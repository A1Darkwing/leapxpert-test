package com.example.message_worker.domain.kafka;

import com.example.purchase_prepaid_data_common.model.MessageAuditReport;
import com.example.purchase_prepaid_data_common.util.JsonUtil;
import com.example.message_worker.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaImportMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaImportMessageConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = "${kafka.message-import.topic}")
    public void receiveImportMessage(String message) {
        logger.info("Received import message='{}'", message);
        MessageAuditReport importMessage = JsonUtil.toObject(message, MessageAuditReport.class);
        // TODO: Handle failure
        messageService.processImportMessage(importMessage);
    }
}

