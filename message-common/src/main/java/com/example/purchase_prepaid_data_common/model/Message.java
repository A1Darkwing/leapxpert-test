package com.example.purchase_prepaid_data_common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String id;
    private String chatRoomId;
    private String senderPhoneNumber;
    private String conversation;
    private long timestamp;
    private int receiverType;  // 1 = INDIVIDUAL, 2 = GROUP
    private int messageType;   // 1 = TEXT, 2 = ATTACHMENT
}
