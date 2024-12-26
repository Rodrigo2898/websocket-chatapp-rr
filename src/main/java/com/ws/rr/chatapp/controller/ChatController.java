package com.ws.rr.chatapp.controller;

import com.ws.rr.chatapp.dto.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    private final RedisTemplate redisTemplate;

    public ChatController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Send message to the clients
    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // Logic to send message to Dragonfly DB queue
        redisTemplate.convertAndSend("chat", chatMessage);
        return chatMessage;
    }

    // Add user to the application
}
