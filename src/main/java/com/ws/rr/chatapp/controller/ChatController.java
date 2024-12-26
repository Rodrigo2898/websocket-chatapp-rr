package com.ws.rr.chatapp.controller;

import com.ws.rr.chatapp.dto.ChatMessage;
import com.ws.rr.chatapp.dto.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
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
    @MessageMapping("/chat.adduser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Get user name from the chat message object and add it to the Websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUserName());
        chatMessage.setMessageType(MessageType.JOIN);
        chatMessage.setMessage(chatMessage.getUserName() + " joined the chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("User joined: {}", chatMessage.getUserName());

        // Send the chat message back to the clients with Message Type as JOIN
        redisTemplate.convertAndSend("chat", chatMessage);
        return chatMessage;
    }
}
