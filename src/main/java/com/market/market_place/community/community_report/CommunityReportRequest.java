package com.market.market_place.community.community_report;

import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.members.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CommunityReportRequest {

    // 신고등록
    @Data
    public static class CreateDTO {
        @NotBlank(message = "신고 사유는 필수입니다.")
        private String reason;

        public CommunityReport toEntity(CommunityPost post, Member reporter){
            return CommunityReport.builder()
                    .post(post)
                    .reason(reason)
                    .reporter(reporter)
                    .status(CommunityReportStatus.PENDING)
                    .build();
        }
    }
}
