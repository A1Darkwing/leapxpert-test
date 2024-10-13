package com.example.message_service.repository;

import com.example.purchase_prepaid_data_common.model.ChatRoom;
import com.example.purchase_prepaid_data_common.model.MessageAuditReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Truy vấn thông tin phòng chat từ bảng chat_rooms bằng chatRoomId
    public ChatRoom findRoomById(String chatRoomId) {
        String sql = "SELECT id, name, type FROM chat_rooms WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{chatRoomId}, (rs, rowNum) -> {
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setId(rs.getString("id"));
                chatRoom.setName(rs.getString("name"));
                chatRoom.setType(rs.getInt("type"));
                return chatRoom;
            });
        } catch (EmptyResultDataAccessException e) {
            // TODO: create chat room?
            return null;
        }
    }

    public List<MessageAuditReport> getAuditReportsBetween(long startTime, long endTime) {
        String sql = "SELECT message_id, chat_room_id, sender_name, sender_phone, recipient, room_name, room_type, conversation, timestamp "
                + "FROM message_audit_reports "
                + "WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp ASC";

        return jdbcTemplate.query(sql, new Object[] {startTime, endTime}, (rs, rowNum) -> {
            MessageAuditReport report = new MessageAuditReport();
            report.setMessageId(rs.getString("message_id"));
            report.setChatRoomId(rs.getString("chat_room_id"));
            report.setSenderName(rs.getString("sender_name"));
            report.setSenderPhoneNumber(rs.getString("sender_phone"));
            report.setRecipient(rs.getString("recipient"));
            report.setRoomName(rs.getString("room_name"));
            report.setRoomType(rs.getString("room_type"));
            report.setConversation(rs.getString("conversation"));
            report.setTimestamp(rs.getLong("timestamp"));
            return report;
        });
    }



}

