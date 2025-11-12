package markit.community.community_comment;

import markit.community.community_post.CommunityPost;
import markit.members.domain.Member;
import lombok.Data;

public class CommunityCommentRequest {

    @Data
    public static class SaveDTO{
        private String content;

        public CommunityComment toEntity(Member member, CommunityPost post) {
            return CommunityComment.builder()
                    .content(content.trim())
                    .member(member)
                    .post(post)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO{
        private String content;
    }
}
