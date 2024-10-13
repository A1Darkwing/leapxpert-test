package com.example.message_service.service;

import com.example.message_service.domain.dto.chat.ImportMessageRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface MessageService {
    String importMessage(ImportMessageRequest request);

    ByteArrayOutputStream getChatMessagingReportAsCsv(String startDate, String endDate) throws IOException;
}

