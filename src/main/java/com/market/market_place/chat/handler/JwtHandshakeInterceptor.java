package com.market.market_place.chat.handler;

import com.market.market_place._core._utils.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // URL 쿼리에서 token 꺼내기
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String header = authHeaders.get(0); // "Bearer xxx"
            if (header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(token);
                    attributes.put("userId", sessionUser.getId());
                    return true;
                } catch (Exception e) {
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
