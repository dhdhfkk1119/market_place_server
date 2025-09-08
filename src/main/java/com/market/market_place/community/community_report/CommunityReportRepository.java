package com.market.market_place.community.community_report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityReportRepository extends JpaRepository<CommunityReport, Long> {

    boolean existsByReporterIdAndPostId(Long reporterId, Long postId);

    Page<CommunityReport> findAllByReporterIdOrderByCreatedAtDesc(Long reporterId, Pageable pageable);
}
