package com.market.market_place.chat._config;

import com.market.market_place.chat.handler.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker // STOMP를 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커가 /topic, /queue 로 시작하는 메시지를 구독자들에게 전달
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트가 서버로 메시지를 보낼 때 사용하는 접두사 (예: /app/chat)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("api/ws-stomp").setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS();
    }


}

