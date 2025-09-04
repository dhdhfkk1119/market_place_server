package com.market.market_place._core._config;

import com.market.market_place._core.auth.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // 인터셉터를 모든 API 경로에 적용합니다.
                .addPathPatterns("/api/**")
                // 인증이 필요 없는 경로
                .excludePathPatterns(
                        // 가입,로그인,평가
                        "/api/members/register",
                        "/api/members/login",
                        "/api/members/health",
                        // 약관
                        "/api/terms",
                        "/api/terms/{id}",
                        "/api/chat/rooms/**",
                        // 임시(AI 채팅)
                        "/api/ai-agent/**",
                        // 토큰 재발급
                        "/api/auth/reissue"
                );
    }
}
