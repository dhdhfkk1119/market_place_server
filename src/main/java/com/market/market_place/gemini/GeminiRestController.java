package com.market.market_place.gemini;

import com.market.market_place._core._utils.SseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/ai-agent/gemini")
@Tag(name = "Gemini 응답 API 서비스", description = "Sse와 연계하여 실시간으로 응답을 내려주는 API")
public class GeminiRestController {

    private final SseUtil sseUtil;
    private final GeminiService geminiService;

    // 생성자
    public GeminiRestController(GeminiService geminiService, SseUtil sseUtil) {
        this.geminiService = geminiService;
        this.sseUtil = sseUtil;
    }

    @Operation(summary = "Sse 구독 API")
    @GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {
        return sseUtil.addEmitter(userId);
    }

    @Operation(summary = "AI 답변 응답 API")
    @PostMapping("/chat/{userId}")
    public ResponseEntity<Void> chat(
            @PathVariable String userId,
            @RequestBody GeminiRequest geminiRequest
    ) {
            geminiService.askGeminiAndSendStreaming(userId, geminiRequest);
            return ResponseEntity.ok().build();
    }
}
