package com.market.market_place.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            log.error("Gemini API Key 확인 실패 userId: {}", userId);
            sseUtil.sendToUser(userId, "error", "Gemini API KEY를 확인할 수 없습니다.");
            return;
        }

        sseUtil.sendToUser(userId, "thinking", "AI가 이미지를 분석하고 있어요...");

        webClient.post()
                .uri(proStreamApiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .map(list -> String.join("", list))
                .flatMap(fullJsonArrayString -> {
                    log.info("FINAL ASSEMBLED STRING: {}", fullJsonArrayString);
                    try {
                        List<GeminiImageResponse> responses = objectMapper.readValue(fullJsonArrayString, new TypeReference<>() {});
                        return reactor.core.publisher.Mono.just(responses);
                    } catch (Exception e) {
                        return reactor.core.publisher.Mono.error(e);
                    }
                })
                .doOnSuccess(responses -> {
                    responses.forEach(chunk -> {
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
                    });
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