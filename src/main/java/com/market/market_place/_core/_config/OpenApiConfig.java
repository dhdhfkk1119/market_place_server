package com.market.market_place._core._config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        // 1. API 문서의 기본 정보 설정
        Info info = new Info()
                .title("Market Place API")
                .description("마켓 플레이스 프로젝트 RESTful API 명세서")
                .version("v1.0.0");

        // 2. JWT 인증 방식을 API 문서에 정의
        // description에 설명을 추가하여 사용자에게 토큰 사용법을 안내
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("로그인 후 발급받은 JWT 토큰을 입력하세요. 'Bearer '를 제외하고 토큰 값만 입력하면 됩니다.");

        // 3. API 전체에 JWT 인증 적용
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("jwtAuth");

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("jwtAuth", securityScheme))
                .addSecurityItem(securityRequirement); // 이 부분을 추가하여 JWT 인증이 필요한 API를 명시
    }
}
