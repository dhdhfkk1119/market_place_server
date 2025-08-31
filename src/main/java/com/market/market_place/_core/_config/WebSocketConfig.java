package com.market.market_place._core._config;

import com.market.market_place._core.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket // WebSocket 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("[+] 최초 WebSocket 연결을 위한 등록 Handler");
        // 사용자가 웹 소켓 연결을 위해 "ws-stomp" 라는 엔드포인트로 연결을 시도하면
        // webSocketHandler 에서 처리
        registry.addHandler(webSocketHandler,"ws-stomp")
                // 접속 시도하는 모든 출처를 허용
                .setAllowedOrigins("*");
    }
}
