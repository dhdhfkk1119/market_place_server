package com.market.market_place.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._utils.SseUtil;
import com.market.market_place._core._utils.TranslationUtil;
import com.market.market_place.gemini.image_chat.GeminiImageRequest;
import com.market.market_place.gemini.image_chat.GeminiImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {
    private final SseUtil sseUtil;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final TranslationUtil translationUtil;

    @Value("${ai.gemini.key}")
    private String apiKey;

    @Value("${ai.gemini.url.flash-stream}")
    private String flashStreamApiUrl;

    @Value("${ai.gemini.url.pro-stream}")
    private String proStreamApiUrl;

    @Async
    public void askImageForGeminiStreaming(String userId, GeminiImageRequest request) {
        if (proStreamApiUrl.trim().isEmpty() || apiKey.trim().isEmpty()) {
            throw new Exception400("Stream API 엔드포인트 또는 API Key가 누락된 잘못된 요청입니다. 설정을 확인해주세요.");
        }

        sseUtil.sendToUser(userId, "thinking", "AI가 이미지를 분석하고 있어요...");

        webClient.post()
                .uri(proStreamApiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(
                        e -> e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.ServiceUnavailable,
                        fallback -> {
                            log.warn("Pro 모델(503) 실패. Flash 모델(플랜 B)로 폴백합니다.");
                            sseUtil.sendToUser(userId, "thinking", "Flash 모델로 다시 시도합니다...");

                            return webClient.post()
                                    .uri(flashStreamApiUrl + "?key=" + apiKey)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(request)
                                    .retrieve()
                                    .bodyToFlux(String.class);
                        }
                )
                .flatMap(chunk -> reactor.core.publisher.Flux.fromArray(chunk.split("\\r?\\n")))
                .filter(line -> !line.trim().isEmpty())
                .flatMap(line -> {
                    try {
                        GeminiImageResponse response = objectMapper.readValue(line, GeminiImageResponse.class);
                        return reactor.core.publisher.Mono.just(response);
                    } catch (Exception e) {
                        log.warn("Gemini 스트림 JSON 파싱 실패, 청크 무시: {}", line, e);
                        return reactor.core.publisher.Mono.empty();
                    }
                })
                .doOnNext(chunk -> {
                    if (chunk.isThinking()) {
                        String thoughtText = chunk.extractThoughtText();
                        String subject = extractSubjectFromThought(thoughtText);
                        String translatedSubject = translationUtil.translateText(subject, "ko");
                        sseUtil.sendToUser(userId, "thinking", translatedSubject);

                    } else {
                        String textChunk = chunk.extractText();
                        if (textChunk != null && !textChunk.isEmpty()) {
                            sseUtil.sendToUser(userId, "AI Response", textChunk);
                        }
                    }
                })
                .doOnError(error -> {
                    log.error("Gemini API 처리 중 최종 에러 발생! userId: {}", userId, error);
                    sseUtil.sendToUser(userId, "error", "AI 서버와 통신 중 문제가 발생했습니다.");
                })
                .doFinally(signalType -> {
                    log.info("스트림 종료. userId: {}", userId);
                    sseUtil.sendToUser(userId, "final", "stream_end");
                })
                .subscribe();
    }

    private String extractSubjectFromThought(String thoughtText) {
        if (thoughtText == null || thoughtText.trim().isEmpty() || !thoughtText.contains("**")) {
            return "내용 분석 중...";
        }
        try {
            String[] parts = thoughtText.split("\\*\\*");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        } catch (Exception e) {
            log.warn("사고 과정 주제 추출 실패: {}", thoughtText);
        }
        return "내용 분석 중...";
    }
}