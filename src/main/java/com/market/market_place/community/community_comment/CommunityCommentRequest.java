package com.market.market_place.community.community_comment;

import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.members.domain.Member;
import lombok.Data;

public class CommunityCommentRequest {

    // 저장
    @Data
    public static class SaveDTO{
        private String content;
        private boolean isSecret = false;

        public CommunityComment toEntity(Member member, CommunityPost post) {
            return CommunityComment.builder()
                    .content(content.trim())
                    .member(member)
                    .post(post)
                    .build();
        }
    }

    // 수정
    @Data
    public static class UpdateDTO{
        private String content;
        private boolean isSecret = false;
    }
}
