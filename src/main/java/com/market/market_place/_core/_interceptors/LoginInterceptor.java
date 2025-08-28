package com.market.market_place._core._interceptors;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. "Authorization" 헤더에서 토큰 추출
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new Exception401("토큰이 없거나 형식이 유효하지 않습니다.");
        }

        String jwt = header.substring(7);

        // 2. 토큰 검증
        try {
            DecodedJWT decodedJWT = jwtUtil.verify(jwt);

            // 3. (선택사항) 검증된 사용자 정보를 요청 속성에 저장하여 컨트롤러에서 사용
            Long memberId = decodedJWT.getClaim("id").asLong();
            request.setAttribute("memberId", memberId);

            return true; // 요청 계속 진행
        } catch (Exception e) {
            throw new Exception401("토큰이 유효하지 않습니다.");
        }
    }
}
