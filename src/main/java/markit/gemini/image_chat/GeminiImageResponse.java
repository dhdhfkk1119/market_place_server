package markit.gemini.image_chat;

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

    public boolean isThinking() {
        if (candidates == null || candidates.isEmpty() ||
                candidates.get(0).content() == null ||
                candidates.get(0).content().parts() == null ||
                candidates.get(0).content().parts().isEmpty()) {
            return false;
        }
        Boolean thoughtFlag = candidates.get(0).content().parts().get(0).thought();
        return thoughtFlag != null && thoughtFlag;
    }

    public String extractText() {
        if (isThinking() || candidates == null || candidates.isEmpty() ||
                candidates.get(0).content() == null ||
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

    public String extractThoughtText() {
        if (isThinking()) {
            return candidates.get(0).content().parts().get(0).text();
        }
        return "";
    }
}