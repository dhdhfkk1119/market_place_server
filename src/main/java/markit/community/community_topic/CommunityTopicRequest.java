package markit.community.community_topic;

import markit.community.community_category.CommunityCategory;
import lombok.Data;

public class CommunityTopicRequest {

    @Data
    public static class SaveDTO{
        private String name;
        private Long categoryId;

        public CommunityTopic toEntity(CommunityCategory category) {
            return CommunityTopic.builder()
                    .name(this.name)
                    .category(category)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String name;
        private Long categoryId;
    }
}
