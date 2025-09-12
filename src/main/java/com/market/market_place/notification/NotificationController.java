package com.market.market_place.notification;

import com.market.market_place._core._utils.SseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
// CORS 모두 허용
@CrossOrigin(origins = "*")
public class NotificationController {

    private final SseUtil sseUtil;

    // 구독하기
    @Operation(summary = "Sse 구독 API")
    @GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {
        return sseUtil.addEmitter(userId);
    }

    // 이 밑에 추가로 만들면 안됨, 있어봤자 코드가 더 많아지는 단점이 있음
}
