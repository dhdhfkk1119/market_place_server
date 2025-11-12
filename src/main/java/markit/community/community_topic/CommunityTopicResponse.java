package markit.community.community_topic;

import lombok.Builder;
import lombok.Data;

public class CommunityTopicResponse {

    @Data
    public static class ListDTO {
        private Long id;
        private String name;

        @Builder
        public ListDTO(CommunityTopic topic) {
            this.id = topic.getId();
            this.name = topic.getName();
        }
    }

    @Data
    public static class TopicResponseDTO{
        private Long id;
        private String name;

        @Builder
        public TopicResponseDTO(CommunityTopic topic) {
            this.id = topic.getId();
            this.name = topic.getName();
        }
    }
}
