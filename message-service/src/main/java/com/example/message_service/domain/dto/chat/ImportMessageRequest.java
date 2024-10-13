package com.example.message_service.domain.dto.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class ImportMessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Sender sender;           // Đối tượng sender
    private Receiver receiver;       // Đối tượng receiver
    private Message message;         // Đối tượng message

    // Inner class cho Sender
    @Data
    public static class Sender {
        private String phoneNumber;  // Số điện thoại của người gửi
        private String displayName;  // Tên hiển thị của người gửi
    }

    // Inner class cho Receiver
    @Data
    public static class Receiver {
        private String type;         // Loại người nhận (INDIVIDUAL hoặc GROUP)
        private String id;           // ID của người nhận
    }

    // Inner class cho Message
    @Data
    public static class Message {

        @NotBlank(message = "ID cannot be blank")
        private String id;                 // ID của tin nhắn
        private long timestamp;            // Thời gian gửi tin nhắn (epoch time)
        private String conversation;       // Nội dung tin nhắn
        private List<Attachment> attachments;  // Danh sách các tệp đính kèm (nếu có)

        // Inner class cho Attachment
        @Data
        public static class Attachment {
            private String url;           // URL của tệp đính kèm
            private String contentType;   // Loại nội dung (ví dụ: image/png, application/pdf)
            private String name;          // Tên của tệp đính kèm
            private long size;            // Kích thước của tệp đính kèm (tính bằng byte)
        }
    }
}
