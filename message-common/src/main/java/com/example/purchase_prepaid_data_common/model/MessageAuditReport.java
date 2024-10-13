package com.example.purchase_prepaid_data_common.model;

import lombok.Data;

@Data
public class MessageAuditReport {
    private String messageId;          // ID của tin nhắn
    private String chatRoomId;         // ID của phòng chat
    private String senderName;         // Tên người gửi
    private String senderPhoneNumber;  // Số điện thoại người gửi
    private String recipient;          // Người nhận (số điện thoại hoặc tên group)
    private String roomName;           // Tên phòng (nếu là chat nhóm)
    private String roomType;           // Loại phòng (GROUP hoặc INDIVIDUAL)
    private String conversation;       // Nội dung (text hoặc tên file đính kèm)
    private long timestamp;            // Thời gian gửi (epoch time)
}


