package com.example.message_worker.repository;

import com.example.purchase_prepaid_data_common.model.Message;
import com.example.purchase_prepaid_data_common.model.MessageAuditReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertMessage(Message message) {
        String sql = "INSERT INTO messages (id, chat_room_id, sender_phone_number, conversation, timestamp, receiver_type, message_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, message.getId(), message.getChatRoomId(), message.getSenderPhoneNumber(),
                message.getConversation(), message.getTimestamp(), message.getReceiverType(), message.getMessageType());
    }

    public void insertMessageAuditReport(MessageAuditReport report) {
        String sql = "INSERT INTO message_audit_reports (message_id, chat_room_id, sender_name, sender_phone, recipient, room_name, room_type, conversation, timestamp) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                report.getMessageId(),
                report.getChatRoomId(),
                report.getSenderName(),
                report.getSenderPhoneNumber(),
                report.getRecipient(),
                report.getRoomName(),
                report.getRoomType(),
                report.getConversation(),
                report.getTimestamp()
        );
    }

}

