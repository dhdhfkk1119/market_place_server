package com.market.market_place.gemini.image_chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

// 'generationConfig' 필드를 추가해서 요청 구조를 확장했어.
public record GeminiImageRequest(
        List<Content> contents,
        @JsonProperty("generation_config")
        GenerationConfig generationConfig
) {

    // --- 기존 코드는 여기에 그대로 유지 ---

    /**
     * 요청의 'parts' 목록을 감싸는 Content 구조
     */
    public record Content(List<Part> parts) {}

    /**
     * 실제 데이터 조각 (텍스트 또는 이미지)
     */
    public record Part(
            @JsonProperty("text") String text,
            @JsonProperty("inline_data") InlineData inlineData
    ) {
        public static Part fromText(String text) {
            return new Part(text, null);
        }

        public static Part fromInlineData(String mimeType, String data) {
            return new Part(null, new InlineData(mimeType, data));
        }
    }

    /**
     * Base64로 인코딩된 이미지 데이터를 담는 구조
     */
    public record InlineData(
            @JsonProperty("mime_type") String mimeType,
            @JsonProperty("data") String data
    ) {}

    // --- '사고(Thinking)' 기능을 위한 새로운 부분 ---

    /**
     * 모델의 생성 방식을 설정하는 전체 설정 객체
     */
    public record GenerationConfig(
            @JsonProperty("thinking_config") ThinkingConfig thinkingConfig
    ) {}

    /**
     * '사고' 기능에 대한 세부 설정 객체
     */
    public record ThinkingConfig(
            // 모델이 생각하는 데 사용할 토큰의 양 (예: 8192)
            @JsonProperty("thinking_budget") Integer thinkingBudget,
            // 응답에 사고 과정 요약을 포함할지 여부
            @JsonProperty("include_thoughts") Boolean includeThoughts
    ) {}


    // --- 이 클래스를 더 쉽게 사용할 수 있게 해주는 헬퍼 메서드 ---

    /**
     * '사고' 기능을 활성화한 요청 객체를 쉽게 생성하는 정적 팩토리 메서드
     * @param contents 요청할 내용 (텍스트, 이미지 등)
     * @return '사고' 기능이 활성화된 GeminiImageRequest 객체
     */
    public static GeminiImageRequest withThinkingEnabled(List<Content> contents) {
        // '사고' 기능 활성화! 예산은 8192 토큰, 과정 요약은 포함하도록 설정
        var thinkingConfig = new ThinkingConfig(8192, true);
        var generationConfig = new GenerationConfig(thinkingConfig);
        return new GeminiImageRequest(contents, generationConfig);
    }


    // --- 기존 유틸리티 메서드는 그대로 유지 ---

    public String extractText() {
        try {
            return this.contents.stream()
                    .flatMap(content -> content.parts().stream())
                    .map(Part::text)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    public InlineData extractInlineData() {
        try {
            return this.contents.stream()
                    .flatMap(content -> content.parts().stream())
                    .map(Part::inlineData)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}