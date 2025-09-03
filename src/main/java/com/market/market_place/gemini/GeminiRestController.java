package com.market.market_place.gemini;

import com.market.market_place._core._utils.SseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/ai-agent")
public class GeminiRestController {

    private final SseUtil sseUtil;
    private final GeminiService geminiService;

    // 생성자
    public GeminiRestController(GeminiService geminiService, SseUtil sseUtil) {
        this.geminiService = geminiService;
        this.sseUtil = sseUtil;
    }

    @GetMapping(value = "/gemini/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {
        return sseUtil.addEmitter(userId);
    }

    @PostMapping("/gemini/chat/{userId}")
    public ResponseEntity<Void> chat(
            @PathVariable String userId,
            @RequestBody GeminiRequest geminiRequest
    ) {
            geminiService.askGeminiAndSendStreaming(userId, geminiRequest);
            return ResponseEntity.ok().build();
    }
}
