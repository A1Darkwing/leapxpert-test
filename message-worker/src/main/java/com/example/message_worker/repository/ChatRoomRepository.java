package com.example.message_worker.repository;

import com.example.purchase_prepaid_data_common.model.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertChatRoom(ChatRoom chatRoom) {
        String sql = "INSERT INTO chat_rooms (id, name, type, created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, chatRoom.getId(), chatRoom.getName(), chatRoom.getType(), chatRoom.getCreatedAt());
    }
}

