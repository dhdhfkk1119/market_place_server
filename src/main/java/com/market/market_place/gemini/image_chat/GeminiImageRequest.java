package com.market.market_place.gemini.image_chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public record GeminiImageRequest(
        List<Content> contents,
        @JsonProperty("generation_config")
        GenerationConfig generationConfig
) {
    public record Content(List<Part> parts) {}

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

    public record InlineData(
            @JsonProperty("mime_type") String mimeType,
            @JsonProperty("data") String data
    ) {}

    public record GenerationConfig(
            @JsonProperty("thinking_config") ThinkingConfig thinkingConfig
    ) {}

    public record ThinkingConfig(
            @JsonProperty("thinking_budget") Integer thinkingBudget,
            @JsonProperty("include_thoughts") Boolean includeThoughts
    ) {}

    public static GeminiImageRequest withThinkingEnabled(List<Content> contents) {
        var thinkingConfig = new ThinkingConfig(8192, true);
        var generationConfig = new GenerationConfig(thinkingConfig);
        return new GeminiImageRequest(contents, generationConfig);
    }

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