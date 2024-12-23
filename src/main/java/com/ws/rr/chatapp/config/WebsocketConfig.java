package com.ws.rr.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra o endpoint STOMP que os clientes podem usar para se conectar via WebSocket
        registry.addEndpoint("/chat-app").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configura o broker de mensagens, habilitando o uso de "/topic/public" como prefixo para a fila pública
        registry.enableSimpleBroker("/topic/public");
        // Define o prefixo para destinos de mensagens da aplicação
        registry.setApplicationDestinationPrefixes("/app");
    }
}
