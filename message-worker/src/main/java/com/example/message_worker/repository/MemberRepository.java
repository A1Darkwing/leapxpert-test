package com.example.message_worker.repository;

import com.example.purchase_prepaid_data_common.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public <T> int[][] batchInsertMembers(List<Member> members, int batchSize) {
        String sql = "INSERT INTO members (phone_number, name, chat_room_id) VALUES (?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, members, batchSize, (ps, member) -> {
            ps.setString(1, member.getPhoneNumber());
            ps.setString(2, member.getName());
            ps.setString(3, member.getChatRoomId());
        });
    }
}

