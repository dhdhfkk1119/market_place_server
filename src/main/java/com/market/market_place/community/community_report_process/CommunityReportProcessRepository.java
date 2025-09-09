package com.market.market_place.community.community_report_process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityReportProcessRepository extends JpaRepository<CommunityReportProcess, Long> {
    List<CommunityReportProcess> findAllByReportId(Long reportId);
    Page<CommunityReportProcess> findAll(Pageable pageable);
}
