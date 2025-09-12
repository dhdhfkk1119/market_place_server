package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_report.CommunityReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySanctionRequest {

    private Long reportedMemberId;

    private Long reportId;

    private String reason;

    private CommunityReportStatus status;


}
