package com.example.purchase_prepaid_data_common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ChatRoom {
    private String id;
    private String name;
    private int type;  // 1 = INDIVIDUAL, 2 = GROUP
    private Timestamp createdAt;

    // Constructor
    public ChatRoom(String id, String name, int type, long createdAtEpoch) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = new Timestamp(createdAtEpoch * 1000);
    }
}
