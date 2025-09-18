package com.market.market_place.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${ai.gemini.url.stream}")
    private String streamApiUrl;

    @Async
    public void askImageForGeminiStreaming(String userId, GeminiImageRequest request) {
        if (streamApiUrl.trim().isEmpty() || apiKey.trim().isEmpty()) {
            throw new Exception400("Stream API 엔드포인트 또는 API Key가 누락된 잘못된 요청입니다. 설정을 확인해주세요.");
        }

        sseUtil.sendToUser(userId, "thinking", "AI가 이미지를 분석하고 있어요...");

        webClient.post()
                .uri(streamApiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class) // 응답을 String 조각으로 받는다.
                .collectList() // 스트림이 끝날 때까지 모든 String 조각을 List로 모은다.
                // --- 여기가 최종 수정! 쉼표 없이 그냥 그대로 합친다 ---
                .map(list -> String.join("", list))
                .flatMap(fullJsonArrayString -> {
                    log.info("FINAL ASSEMBLED STRING: {}", fullJsonArrayString);
                    try {
                        // 이제 fullJsonArrayString은 "[{...},{...}]" 형태의 완벽한 문자열이다.
                        List<GeminiImageResponse> responses = objectMapper.readValue(fullJsonArrayString, new TypeReference<>() {});
                        return reactor.core.publisher.Mono.just(responses);
                    } catch (Exception e) {
                        log.error("최종 JSON 배열 파싱 실패. 응답 문자열: {}", fullJsonArrayString);
                        return reactor.core.publisher.Mono.error(e);
                    }
                })
                .doOnSuccess(responses -> {
                    responses.forEach(chunk -> {
                        if (chunk.isThinking()) {
                            String thoughtText = chunk.extractThoughtText();
                            String subject = extractSubjectFromThought(thoughtText);

                            // --- 여기가 핵심! ---
                            // 영어로 된 주제를 한국어로 번역한다.
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
                    log.error("Gemini API 처리 중 에러 발생! userId: {}", userId, error);
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