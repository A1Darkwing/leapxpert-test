package com.example.message_service.domain.kafka;

import com.example.purchase_prepaid_data_common.model.MessageAuditReport;
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
public class KafkaImportMessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaImportMessagePublisher.class);

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public void publish(String topic, int key, MessageAuditReport importMessage) {
        final String importMessageMsg = JsonUtil.toJsonString(importMessage);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate
                .send(topic, key, importMessageMsg);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                logger
                        .info("KafkaImportMessagePublisher#publish success push message to queue request='{}' with offset={}", importMessageMsg,
                                result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("KafkaImportMessagePublisher#publish failed push message to queue request='{}'", importMessageMsg, ex);
            }
        });
    }
}
