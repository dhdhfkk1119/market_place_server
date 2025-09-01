package com.market.market_place._core._config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import org.apache.hc.client5.http.classic.HttpClient;
import java.time.Duration;

@Configuration
public class AppConfig {

    // RestTemplate Bean 등록
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        HttpClient httpClient = HttpClients.createDefault();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectionRequestTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(5));

        return builder
                .requestFactory(() -> factory)
                .build();
    }

    // WebClient Bean 등록
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
