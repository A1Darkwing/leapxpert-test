package com.example.message_worker.service.impl;

import com.example.purchase_prepaid_data_common.cache.MessageMap;
import com.example.purchase_prepaid_data_common.enumerate.MessageStatus;
import com.example.purchase_prepaid_data_common.model.MessageAuditReport;
import com.example.message_worker.domain.cache.MessageCache;
import com.example.message_worker.externalapi.thirdpartiessystem.usersystem.create.CreateUserApi;
import com.example.message_worker.externalapi.thirdpartiessystem.usersystem.create.CreateUserRequest;
import com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter.FilterApi;
import com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter.FilterRequest;
import com.example.message_worker.externalapi.thirdpartiessystem.usersystem.filter.FilterResponse;
import com.example.message_worker.repository.MessageRepository;
import com.example.message_worker.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageCache messageCache;

    @Autowired private FilterApi userFilterApi;

    @Autowired private CreateUserApi createUserApi;

    @Override
    public String processImportMessage(MessageAuditReport messageAuditReport) {
        String messageId = messageAuditReport.getMessageId();
        logger.info("Starting to process import message ID: {}", messageId);

        try {
            // Cập nhật trạng thái tin nhắn là đang xử lý
            updateStatusCache(messageId, MessageStatus.PROCESSING);

            // Chèn dữ liệu vào bảng message_audit_reports
            messageRepository.insertMessageAuditReport(messageAuditReport);

            // Cập nhật trạng thái tin nhắn là thành công
            updateStatusCache(messageId, MessageStatus.SUCCESS);

            // Tạo user nếu cần
            createUserIfNeeded(messageAuditReport.getSenderPhoneNumber(), messageAuditReport.getSenderName());
            return "Message processed successfully with ID: " + messageId;

        } catch (Exception e) {
            // Cập nhật trạng thái tin nhắn là thất bại
            updateStatusCache(messageId, MessageStatus.FAIL);
            logger.error("Error processing import message ID: {}. Error: {}", messageId, e.getMessage(), e);
            return "Error processing message with ID: " + messageId;
        }
    }

    private void updateStatusCache(String messageId, MessageStatus status) {
        boolean result = messageCache.saveImportMessage(new MessageMap(messageId, status.getValue()));
        if (!result) {
            logger.error("Error caching message ID: {}", messageId);
        }
    }

    @Async
    protected void createUserIfNeeded(String phoneNumber, String name) {
        // TODO : add logic save response to cache and check cache before calling.
        FilterResponse res = userFilterApi.call(FilterRequest.builder()
                .phoneNumber(phoneNumber)
                .build());
        if (res.getStatusCode() == 404) {
            createUserApi.call(CreateUserRequest.builder()
                    .phoneNumber(phoneNumber)
                    .name(name)
                    .build());
        }
    }

}

