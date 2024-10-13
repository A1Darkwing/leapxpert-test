package com.example.message_service.domain.kafka;

import com.example.purchase_prepaid_data_common.model.CreateChatRoomMessage;
import com.example.purchase_prepaid_data_common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaChatRoomCreatePublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaChatRoomCreatePublisher.class);

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public void publish(String topic, CreateChatRoomMessage chatRoom) {
        final String chatRoomSerializeMsg = JsonUtil.toJsonString(chatRoom);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate
                .send(topic, chatRoomSerializeMsg);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                logger
                        .info("KafkaChatRoomCreatePublisher#publish success create chat room request='{}' with offset={}", chatRoomSerializeMsg,
                                result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("KafkaPurchasePublisher#publish failed create chat room request='{}'", chatRoomSerializeMsg, ex);
            }
        });
    }
}
