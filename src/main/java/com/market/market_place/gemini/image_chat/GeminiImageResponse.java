package com.market.market_place.gemini.image_chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeminiImageResponse(
        List<Candidate> candidates
) {
    // Candidate 구조
    public record Candidate(
            Content content
    ) {}

    // Content 구조 (요청 DTO의 것을 재사용)
    public record Content(
            List<Part> parts
    ) {}

    // Part 구조 (요청 DTO의 것을 재사용)
    public record Part(
            @JsonProperty("text")
            String text
    ) {}

    // 응답 텍스트를 쉽게 꺼내기 위한 편의 메서드
    public String extractText() {
        if (this.candidates != null && !this.candidates.isEmpty()) {
            Candidate firstCandidate = this.candidates.get(0);
            if (firstCandidate.content() != null && !firstCandidate.content().parts().isEmpty()) {
                return firstCandidate.content().parts().get(0).text();
            }
        }
        return "Gemini가 응답을 생성하지 않았습니다.";
    }
}
