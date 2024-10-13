package com.example.message_service.controller;

import com.example.message_service.domain.dto.chat.ImportMessageRequest;
import com.example.message_service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/import")
    public String importMessage(@RequestBody ImportMessageRequest request) {
        return messageService.importMessage(request);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportChatMessagingReportAsCsv(
            @RequestParam String startDate,
            @RequestParam String endDate) throws IOException {

        // Lấy dữ liệu báo cáo từ service
        ByteArrayOutputStream csvData = messageService.getChatMessagingReportAsCsv(startDate, endDate);

        // Tạo header cho file CSV
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chat_messaging_report.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

        // Trả về response
        return ResponseEntity.ok().headers(headers).body(csvData.toByteArray());

    }
}

