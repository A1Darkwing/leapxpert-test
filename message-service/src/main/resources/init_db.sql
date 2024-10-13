CREATE TABLE chat_rooms
(
    id         VARCHAR(255) PRIMARY KEY, -- ID của phòng chat
    name       VARCHAR(255),             -- Tên phòng chat
    type       INT,                      -- Loại phòng chat (1 = INDIVIDUAL, 2 = GROUP)
    created_at TIMESTAMP                 -- Thời gian tạo phòng chat
);

CREATE INDEX idx_chat_rooms_type ON chat_rooms (type);
CREATE INDEX idx_chat_rooms_created_at ON chat_rooms (created_at);

CREATE TABLE members
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT, -- ID tự động tăng của member
    phone_number VARCHAR(20),                       -- Số điện thoại của thành viên
    name         VARCHAR(255),                      -- Tên của thành viên
    chat_room_id VARCHAR(255)                       -- ID của phòng chat mà member thuộc về
);

CREATE INDEX idx_members_chat_room_id ON members (chat_room_id);
CREATE INDEX idx_members_phone_number ON members (phone_number);

CREATE TABLE message_audit_reports
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id   VARCHAR(255),                       -- ID của tin nhắn
    chat_room_id VARCHAR(255),                       -- ID của phòng chat
    sender_name  VARCHAR(255),                       -- Tên người gửi
    sender_phone VARCHAR(20),                        -- Số điện thoại người gửi
    recipient    VARCHAR(255),                       -- Người nhận (số điện thoại hoặc tên group)
    room_name    VARCHAR(255),                       -- Tên phòng (nếu là chat nhóm)
    room_type    VARCHAR(50),                        -- Loại phòng (GROUP hoặc INDIVIDUAL)
    conversation TEXT,                               -- Nội dung (text hoặc tên file đính kèm)
    timestamp    BIGINT,                             -- Thời gian gửi (epoch time)
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Thời gian lưu vào bảng
);

CREATE INDEX idx_message_id ON message_audit_reports (message_id);
CREATE INDEX idx_chat_room_id ON message_audit_reports (chat_room_id);
CREATE INDEX idx_sender_phone ON message_audit_reports (sender_phone);
CREATE INDEX idx_timestamp ON message_audit_reports (timestamp);