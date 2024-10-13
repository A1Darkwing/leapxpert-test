package com.example.message_service.controller;

import com.example.message_service.domain.dto.chat.CreateChatRoomRequest;
import com.example.message_service.service.ChatRoomService;
import com.example.message_service.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create")
    public String createChatRoom(@Valid @RequestBody final CreateChatRoomRequest request, @ApiIgnore Errors errors) {
        ValidationUtils.handleValidationResult(errors);
        return chatRoomService.createChatRoom(request);
    }
}

