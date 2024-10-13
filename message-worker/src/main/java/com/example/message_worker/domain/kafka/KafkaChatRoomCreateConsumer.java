package com.example.message_worker.domain.kafka;

import com.example.purchase_prepaid_data_common.model.CreateChatRoomMessage;
import com.example.purchase_prepaid_data_common.util.JsonUtil;
import com.example.message_worker.domain.cache.ChatRoomCache;
import com.example.message_worker.service.ChatRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaChatRoomCreateConsumer {

  private static final Logger logger = LoggerFactory.getLogger(KafkaChatRoomCreateConsumer.class);

  @Autowired
  private ChatRoomService chatRoomService;

  @Autowired
  private ChatRoomCache chatRoomCache;

  @KafkaListener(topics = "${kafka.chatroom.topic}")
  public void receiveMessage(String message) {
    logger.info("received message='{}'", message);
    CreateChatRoomMessage createChatRoomMessage = JsonUtil.toObject(message, CreateChatRoomMessage.class);
    // TODO: handle failure :D
    chatRoomService.createChatRoom(createChatRoomMessage);
  }
}
