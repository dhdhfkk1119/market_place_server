package com.market.market_place.moderation.sanction.community_sanction;

import com.market.market_place.community.community_report.CommunityReportRepository;
import com.market.market_place.community.community_report.CommunityReportStatus;
import com.market.market_place.community.community_report_process.CommunityReportProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunitySanctionService {

    private final CommunitySanction communitySanction;
    private final CommunityReportProcessRepository communityReportProcessRepository;
    private final CommunityReportRepository communityReportRepository;



}
