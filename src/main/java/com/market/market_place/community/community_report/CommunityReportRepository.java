package com.market.market_place.community.community_report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityReportRepository extends JpaRepository<CommunityReport, Long> {

    boolean existsByReporterIdAndPostId(Long reporterId, Long postId);

    Page<CommunityReport> findAllByReporterIdOrderByCreatedAtDesc(Long reporterId, Pageable pageable);

    // 신고자와 신고 답변 한번에 조회
    @Query("SELECT r FROM CommunityReport r LEFT JOIN FETCH r.adminComments c LEFT JOIN FETCH c.admin WHERE r.id = :reporterId")
    Optional<CommunityReport> findByIdWithAdminComments(@Param("reporterId")Long reporterId);

}
