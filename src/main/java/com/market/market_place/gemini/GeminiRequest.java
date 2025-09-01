package com.market.market_place.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public record GeminiRequest (
        List<Content> contents
) {

    /**
     * 요청의 'parts' 목록을 감싸는 Content 구조
     */
    record Content(
            List<Part> parts
    ) {}

    /**
     * 실제 데이터 조각 (텍스트 또는 이미지)
     */
    record Part(
            @JsonProperty("text")
            String text,

            @JsonProperty("inline_data")
            InlineData inlineData
    ) {
        /**
         * 텍스트 파트를 쉽게 만들기 위한 정적 메서드
         */
        public static Part fromText(String text) {
            return new Part(text, null);
        }

        /**
         * 이미지 파트를 쉽게 만들기 위한 메서드
         */
        public static Part fromInlineData(String mimeType, String data) {
            return new Part(null, new InlineData(mimeType, data));
        }
    }

    /**
     * Base64로 인코딩된 이미지 데이터를 담는 구조
     */
    record InlineData(
            @JsonProperty("mime_type")
            String mimeType,

            @JsonProperty("data")
            String data
    ) {}

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
