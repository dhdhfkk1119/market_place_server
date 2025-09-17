package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_report.CommunityReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySanctionRequest {

    private String reason;
    private CommunityReportStatus status;
}
