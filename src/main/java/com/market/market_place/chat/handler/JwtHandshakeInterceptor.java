package com.market.market_place.chat.handler;

import com.market.market_place._core._utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // 들어오는 모든 헤더를 로그로 출력해봅니다.
        request.getHeaders().forEach((key, value) ->
                log.info("Header: {} -> {}", key, value));

        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String header = authHeaders.get(0); // "Bearer xxx"
            if (header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(token);
                    System.out.println("JWT 검증 성공! 사용자 ID: " + sessionUser.getId());
                    attributes.put("userId", sessionUser.getId());
                    return true;
                } catch (Exception e) {
                    System.err.println(" JWT 검증 실패: " + e.getMessage());
                    e.printStackTrace(); // 예외 스택 트레이스 전체를 출력
                    return false; // 토큰 검증 실패
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {

    }
}
