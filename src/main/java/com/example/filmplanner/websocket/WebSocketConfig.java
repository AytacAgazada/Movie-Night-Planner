package com.example.filmplanner.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final VoteWebSocketHandler voteWebSocketHandler;

    public WebSocketConfig(VoteWebSocketHandler voteWebSocketHandler) {
        this.voteWebSocketHandler = voteWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(voteWebSocketHandler, "/ws/votes").setAllowedOrigins("*");
    }
}