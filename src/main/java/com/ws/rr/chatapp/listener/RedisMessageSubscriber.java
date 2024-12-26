package com.ws.rr.chatapp.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.rr.chatapp.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // logic to broadcast the message to the clients
        String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

        try {
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
