package com.example.message_service.service.impl;

import com.example.purchase_prepaid_data_common.cache.ChatRoomMap;
import com.example.purchase_prepaid_data_common.enumerate.MessageStatus;
import com.example.purchase_prepaid_data_common.model.ChatRoom;
import com.example.purchase_prepaid_data_common.model.CreateChatRoomMessage;
import com.example.purchase_prepaid_data_common.model.Member;
import com.example.message_service.domain.cache.ChatRoomCache;
import com.example.message_service.domain.dto.chat.CreateChatRoomRequest;
import com.example.message_service.domain.kafka.KafkaChatRoomCreatePublisher;
import com.example.message_service.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoomServiceImpl.class);

    @Autowired
    private ChatRoomCache chatRoomCache;

    @Autowired
    private KafkaChatRoomCreatePublisher kafkaChatRoomCreatePublisher;

    @Value("${kafka.chatroom.topic}")
    private String chatRoomCreateTopic;

    @Override
    public String createChatRoom(CreateChatRoomRequest request) {
        try {
            boolean ok = chatRoomCache
                    .saveChatRoomMap(new ChatRoomMap(request.getId(), request.getType(), request.getName(), MessageStatus.CREATED.getValue()));
            if (ok) {
                ChatRoom chatRoom = new ChatRoom(request.getId(), request.getName(), request.getType(), request.getTimestamp());
                List<Member> members = new ArrayList<>();
                for (CreateChatRoomRequest.Member member : request.getMembers()) {
                    Member newMember = new Member(member.getPhoneNumber(), member.getName(), request.getId());
                    members.add(newMember);
                }
                kafkaChatRoomCreatePublisher.publish(chatRoomCreateTopic, CreateChatRoomMessage.builder().chatRoom(chatRoom).members(members).build());
                return "Chat room creating!";
            } else {
                return "Chat room already exists!";
            }
        } catch (Exception e) {
            logger.error("Error creating chat room: {}", e.getMessage());
            return "Error creating chat room!";
        }
    }
}

