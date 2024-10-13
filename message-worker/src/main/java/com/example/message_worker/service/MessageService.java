package com.example.message_worker.service;

import com.example.purchase_prepaid_data_common.model.MessageAuditReport;

public interface MessageService {
    String processImportMessage(MessageAuditReport importMessage);
}

