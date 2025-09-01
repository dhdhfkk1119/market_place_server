package com.market.market_place._core._utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseUtil {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(userId, emitter);

        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        sendToUser(userId, "성공적으로 연결되었습니다! [userId=" + userId + "]");
        return emitter;
    }

    public void removeEmitter(String userId) {
        if (emitters.containsKey(userId)) {
            this.emitters.remove(userId);
            log.info("emitter 제거됨: {}", userId);
        }
    }

    public void sendToUser(String userId, String data) {
        SseEmitter emitter = emitters.get(userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("AI Response").data(data));
            } catch (IOException e) {
                log.error("SSE 전송 중 오류 발생! userId: {}, error: {}", userId, e.getMessage());
                removeEmitter(userId);
            }
        }
    }
}
