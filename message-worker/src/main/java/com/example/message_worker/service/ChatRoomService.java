package com.example.message_worker.service;

import com.example.purchase_prepaid_data_common.model.CreateChatRoomMessage;

public interface ChatRoomService {
    String createChatRoom(CreateChatRoomMessage createChatRoomMessage);
}

