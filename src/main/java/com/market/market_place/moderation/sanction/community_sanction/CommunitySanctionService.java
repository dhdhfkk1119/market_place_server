package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_report.CommunityReportRepository;
import com.market.market_place.community.community_report_process.CommunityReportProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunitySanctionService {

    private final CommunityReportProcessRepository communityReportProcessRepository;
    private final CommunityReportRepository communityReportRepository;
}
