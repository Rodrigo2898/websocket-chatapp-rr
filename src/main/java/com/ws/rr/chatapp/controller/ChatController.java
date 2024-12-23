package com.ws.rr.chatapp.controller;

import com.ws.rr.chatapp.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    // Send message to the clients
    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // Logic to send message to Dragonfly DB queue
        return chatMessage;
    }

    // Add user to the application
}
