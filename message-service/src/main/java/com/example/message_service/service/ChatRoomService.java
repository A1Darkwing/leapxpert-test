package com.example.message_service.service;

import com.example.message_service.domain.dto.chat.CreateChatRoomRequest;

public interface ChatRoomService {
    String createChatRoom(CreateChatRoomRequest request);
}

