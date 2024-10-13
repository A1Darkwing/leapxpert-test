package com.example.message_worker.service.impl;

import com.example.purchase_prepaid_data_common.cache.ChatRoomMap;
import com.example.purchase_prepaid_data_common.enumerate.MessageStatus;
import com.example.purchase_prepaid_data_common.model.CreateChatRoomMessage;
import com.example.message_worker.domain.cache.ChatRoomCache;
import com.example.message_worker.repository.ChatRoomRepository;
import com.example.message_worker.repository.MemberRepository;
import com.example.message_worker.service.ChatRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoomServiceImpl.class);

    private static final int BATCH_SIZE = 100;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomCache chatRoomCache;

    @Override
    public String createChatRoom(CreateChatRoomMessage createChatRoomMessage) {
        long startTime = System.currentTimeMillis();
        try {
            // update date first
            chatRoomCache.saveChatRoomMap(new ChatRoomMap(createChatRoomMessage.getChatRoom().getId(), createChatRoomMessage.getChatRoom().getType(), createChatRoomMessage.getChatRoom().getName(), MessageStatus.PROCESSING.getValue()));
            // insert db
            chatRoomRepository.insertChatRoom(createChatRoomMessage.getChatRoom());
            memberRepository.batchInsertMembers(createChatRoomMessage.getMembers(), BATCH_SIZE);
            // update cache after insert db
            chatRoomCache.saveChatRoomMap(new ChatRoomMap(createChatRoomMessage.getChatRoom().getId(),
                    createChatRoomMessage.getChatRoom().getType(),
                    createChatRoomMessage.getChatRoom().getName(),
                    MessageStatus.SUCCESS.getValue()));
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.info("Chat Room Created Successfully in {} ms", duration);

            return "Chat Room Created Successfully";
        } catch (Exception e) {
            chatRoomCache.saveChatRoomMap(new ChatRoomMap(createChatRoomMessage.getChatRoom().getId(),
                    createChatRoomMessage.getChatRoom().getType(),
                    createChatRoomMessage.getChatRoom().getName(),
                    MessageStatus.FAIL.getValue()));
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.error("Error creating chat room in {} ms: {}", duration, e.getMessage());

            return "Error creating chat room!";
        }
    }

}

