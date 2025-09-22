package com.market.market_place.naver.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/naver")
public class NaverMapController {

    @Value("${naver.api.client-id}")
    private String naverMapClientId;

    private final WebClient webClient = WebClient.create("https://openapi.naver.com");

    @GetMapping("/map/client-id")
    public ResponseEntity<String> getMapClientId() {
        if (naverMapClientId == null || naverMapClientId.trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        log.info("클라이언트 ID 전송 처리");
        return ResponseEntity.ok(naverMapClientId);
    }

    // 아래는 임시, 아직 애매함
    @GetMapping("/search/local")
    public Mono<ResponseEntity<String>> searchLocal(@RequestParam String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/local.json")
                        .queryParam("query", query)
                        .queryParam("display", 5)
                        .build())
                .header("X-Naver-Client-Id", naverMapClientId)
                .retrieve()
                .toEntity(String.class)
                .doOnError(error -> System.err.println("네이버 API 호출 오류: " + error.getMessage()));
    }
}
