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
        // ws-stomp라는 엔드포인트로 STOMP 연결을 할 수 있게 함
        // SockJS를 사용하면 웹소켓을 지원하지 않는 브라우저에서도 통신 가능
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor) // <--- 이 부분을 추가합니다.
                .withSockJS();
    }


}

