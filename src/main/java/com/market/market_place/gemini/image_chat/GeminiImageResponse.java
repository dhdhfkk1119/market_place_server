package com.market.market_place.gemini.image_chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiImageResponse(
        List<Candidate> candidates
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(Content content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(List<Part> parts) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Part(
            String text,
            Boolean thought
    ) {}

    /**
     * 이 응답이 '사고' 과정인지 확인하는 새로운 로직
     */
    public boolean isThinking() {
        if (candidates == null || candidates.isEmpty() ||
                candidates.get(0).content() == null ||
                // --- 여기가 수정된 부분! ---
                // parts가 null인지 먼저 확인해서 NullPointerException을 방지한다.
                candidates.get(0).content().parts() == null ||
                candidates.get(0).content().parts().isEmpty()) {
            return false;
        }
        Boolean thoughtFlag = candidates.get(0).content().parts().get(0).thought();
        return thoughtFlag != null && thoughtFlag;
    }

    /**
     * '사고' 과정이 아닌, 실제 응답 텍스트만 추출하는 메서드
     */
    public String extractText() {
        if (isThinking() || candidates == null || candidates.isEmpty() ||
                candidates.get(0).content() == null ||
                // --- 여기도 똑같이 수정! ---
                candidates.get(0).content().parts() == null ||
                candidates.get(0).content().parts().isEmpty()) {
            return "";
        }

        Part firstPart = candidates.get(0).content().parts().get(0);
        if (firstPart.thought() == null || !firstPart.thought()) {
            return firstPart.text();
        }
        return "";
    }

    /**
     * '사고' 과정의 텍스트만 추출하는 메서드 (isThinking()을 호출하므로 자동 수정됨)
     */
    public String extractThoughtText() {
        if (isThinking()) {
            return candidates.get(0).content().parts().get(0).text();
        }
        return "";
    }
}