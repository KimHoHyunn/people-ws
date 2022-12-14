package com.people.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.people.ws.handler.WebSocketChatHandler;



@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer{

    private final WebSocketChatHandler webSocketHandler;

    public WebsocketConfig(WebSocketChatHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	//ws://127.0.0.1:포트/원하는이름
        registry.addHandler(webSocketHandler, "/people").setAllowedOrigins("*");
    }
}