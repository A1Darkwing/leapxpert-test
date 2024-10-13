package com.example.message_service.service.impl;

import com.example.purchase_prepaid_data_common.cache.ChatRoomMap;
import com.example.purchase_prepaid_data_common.cache.MessageMap;
import com.example.purchase_prepaid_data_common.enumerate.MessageStatus;
import com.example.purchase_prepaid_data_common.model.*;
import com.example.message_service.domain.cache.ChatRoomCache;
import com.example.message_service.domain.cache.MessageCache;
import com.example.message_service.domain.dto.chat.ImportMessageRequest;
import com.example.message_service.domain.kafka.KafkaImportMessagePublisher;
import com.example.message_service.repository.MessageRepository;
import com.example.message_service.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageCache messageCache;

    @Autowired
    private ChatRoomCache chatRoomCache;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaImportMessagePublisher kafkaImportMessagePublisher;

    @Value("${kafka.message-import.topic}")
    private String importMessageTopic;

    @Value("${kafka.attachment-import.topic}")
    private String importAttachmentsTopic;

    @Override
    public String importMessage(ImportMessageRequest importMessageRequest) {
        List<MessageAuditReport> messageAuditReports = createMessageAuditReportFromRequest(importMessageRequest);
        publishMessagesToKafka(messageAuditReports);
        logger.info("Successfully imported message ID: {}", importMessageRequest.getMessage().getId());
        return "Message imported successfully! ID: " + importMessageRequest.getMessage().getId();
    }

    @Override
    public ByteArrayOutputStream getChatMessagingReportAsCsv(String startDate, String endDate) throws IOException {
        List<MessageAuditReport> reports = messageRepository.getAuditReportsBetween(Long.parseLong(startDate), Long.parseLong(endDate));
        return generateCsvFromReports(reports);
    }

    private ByteArrayOutputStream generateCsvFromReports(List<MessageAuditReport> reports) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("Message ID,Chat Room ID,Sender Name,Sender Phone,Recipient,Room Name,Room Type,Conversation,Timestamp");

        for (MessageAuditReport report : reports) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d%n",
                    report.getMessageId(),
                    report.getChatRoomId(),
                    report.getSenderName(),
                    report.getSenderPhoneNumber(),
                    report.getRecipient(),
                    report.getRoomName(),
                    report.getRoomType(),
                    report.getConversation(),
                    report.getTimestamp());
        }

        writer.flush();
        return outputStream;
    }

    private List<MessageAuditReport> createMessageAuditReportFromRequest(ImportMessageRequest importMessageRequest) {
        List<MessageAuditReport> reports = new ArrayList<>();
        MessageAuditReport messageReport = new MessageAuditReport();

        messageReport.setMessageId(importMessageRequest.getMessage().getId());
        messageReport.setChatRoomId(importMessageRequest.getReceiver().getId());
        messageReport.setSenderName(importMessageRequest.getSender().getDisplayName());
        messageReport.setSenderPhoneNumber(importMessageRequest.getSender().getPhoneNumber());

        if (importMessageRequest.getReceiver().getType().equalsIgnoreCase("INDIVIDUAL")) {
            messageReport.setRecipient(importMessageRequest.getReceiver().getId());
            messageReport.setRoomName("");
            messageReport.setRoomType("INDIVIDUAL");
        } else {
            String roomName = getRoomNameFromCacheOrDb(importMessageRequest.getReceiver().getId());
            messageReport.setRecipient(roomName);
            messageReport.setRoomName(roomName);
            messageReport.setRoomType("GROUP");
        }

        messageReport.setTimestamp(importMessageRequest.getMessage().getTimestamp());
        messageReport.setConversation(importMessageRequest.getMessage().getConversation());
        reports.add(messageReport);
        reports.addAll(createAttachmentsAuditReport(importMessageRequest));

        return reports;
    }

    private String getRoomNameFromCacheOrDb(String chatRoomId) {
        ChatRoomMap chatRoomMap = chatRoomCache.getChatRoomMap(chatRoomCache.getChatRoomEntityKey(chatRoomId));
        if (chatRoomMap != null) {
            return chatRoomMap.getName();
        }

        ChatRoom room = messageRepository.findRoomById(chatRoomId);
        if (room != null) {
            chatRoomCache.saveChatRoomMap(new ChatRoomMap(room.getId(), room.getType(), room.getName(), MessageStatus.SUCCESS.getValue()));
            return room.getName();
        }

        return "";
    }

    private List<MessageAuditReport> createAttachmentsAuditReport(ImportMessageRequest importMessageRequest) {
        List<MessageAuditReport> reports = new ArrayList<>();

        if (importMessageRequest.getMessage().getAttachments() == null || importMessageRequest.getMessage().getAttachments().isEmpty()) {
            return reports;
        }

        for (ImportMessageRequest.Message.Attachment attachment : importMessageRequest.getMessage().getAttachments()) {
            MessageAuditReport attachmentReport = new MessageAuditReport();
            attachmentReport.setMessageId(importMessageRequest.getMessage().getId());
            attachmentReport.setChatRoomId(importMessageRequest.getReceiver().getId());
            attachmentReport.setSenderName(importMessageRequest.getSender().getDisplayName());
            attachmentReport.setSenderPhoneNumber(importMessageRequest.getSender().getPhoneNumber());

            if (importMessageRequest.getReceiver().getType().equalsIgnoreCase("INDIVIDUAL")) {
                attachmentReport.setRecipient(importMessageRequest.getReceiver().getId());
                attachmentReport.setRoomName("");
                attachmentReport.setRoomType("INDIVIDUAL");
            } else {
                String roomName = getRoomNameFromCacheOrDb(importMessageRequest.getReceiver().getId());
                attachmentReport.setRecipient(roomName);
                attachmentReport.setRoomName(roomName);
                attachmentReport.setRoomType("GROUP");
            }

            attachmentReport.setTimestamp(importMessageRequest.getMessage().getTimestamp());
            attachmentReport.setConversation(attachment.getName());
            reports.add(attachmentReport);
        }

        return reports;
    }

    private void publishMessagesToKafka(List<MessageAuditReport> messageAuditReports) {
        for (MessageAuditReport report : messageAuditReports) {
            int key = report.getMessageId().hashCode();
            kafkaImportMessagePublisher.publish(importMessageTopic, key, report);
            cacheImportMessage(report.getMessageId());
        }
        logger.info("Published {} message reports to Kafka.", messageAuditReports.size());
    }

    private void cacheImportMessage(String messageId) {
        boolean result = messageCache.saveImportMessage(new MessageMap(messageId, MessageStatus.CREATED.getValue()));
        if (!result) {
            logger.error("Error caching message ID: {}", messageId);
        }
    }
}
