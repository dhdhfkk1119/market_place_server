package com.market.market_place.gemini;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._utils.SseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Slf4j
@Service
public class GeminiService {
    private final SseUtil sseUtil;
    private final WebClient webClient;

    public GeminiService(SseUtil sseUtil, WebClient webClient) {
        this.sseUtil = sseUtil;
        this.webClient = webClient;
    }

    // @Value("${ai.gemini.key}") -> 추후에 다시 주석 해제
    private String apiKey;

    @Value("${ai.gemini.url.stream}")
    private String streamApiUrl;

    @Async
    public void askGeminiAndSendStreaming(String userId, GeminiRequest request) {
        if (streamApiUrl.trim().isEmpty() || apiKey.trim().isEmpty()) {
            throw new Exception400("API 엔드포인트 또는 API Key가 누락된 잘못된 요청입니다. 설정을 확인해주세요.");
        }

        try {
            webClient.post()
                    .uri(streamApiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToFlux(GeminiResponse.class)
                    .subscribe(
                            chunk -> {
                                String textChunk = chunk.extractText();
                                if (textChunk != null && !textChunk.isEmpty()) {
                                    sseUtil.sendToUser(userId, textChunk);
                                }
                            },
                            error -> {
                                sseUtil.sendToUser(userId, "AI 스트리밍 중 오류가 발생했습니다.");
                            },
                            () -> {
                                sseUtil.sendToUser(userId, "stream_end"); // -> 스트림이 끝났다는 신호
                            }
                    );

        } catch (Exception e) {
            sseUtil.sendToUser(userId, "AI 요청 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}