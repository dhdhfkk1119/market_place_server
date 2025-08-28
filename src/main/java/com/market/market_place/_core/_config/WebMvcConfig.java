package com.market.market_place._core._config;

import com.market.market_place._core._interceptors.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // 인터셉터를 적용할 경로 패턴
                .addPathPatterns("/api/**")
                // 인터셉터 적용에서 제외할 경로 패턴
                .excludePathPatterns(
                        "/api/members/register",
                        "/api/members/login",
                        "/api/members/health"
                );
    }
}
