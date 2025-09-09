package com.market.market_place.gemini;

import com.market.market_place._core._utils.SseUtil;
import com.market.market_place.gemini.image_chat.GeminiImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/ai-agent/gemini")
@Tag(name = "Gemini 응답 API 서비스", description = "AI 응답을 내려주는 API")
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

    @Operation(summary = "AI 이미지 분석 전체 응답 API")
    @PostMapping("/image/{userId}")
    public ResponseEntity<Void> imageChat(
            @PathVariable String userId,
            @RequestBody GeminiImageRequest geminiRequest
    ) {
        geminiService.askImageForGemini(userId, geminiRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "AI 이미지 분석 실시간 응답 API")
    @PostMapping("/image/{userId}/stream")
    public ResponseEntity<Void> streamImageChat(
            @PathVariable String userId,
            @RequestBody GeminiImageRequest geminiRequest
    ) {
            geminiService.askImageForGeminiStreaming(userId, geminiRequest);
            return ResponseEntity.ok().build();
    }
}
