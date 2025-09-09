package com.market.market_place.community.community_report;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.community.community_post.CommunityPostRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityReportService {

    private final CommunityReportRepository reportRepository;
    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;

    // 신고 등록
    @Transactional
    public CommunityReportResponse.CreateDTO createReport(Long postId, Long memberId,
                                                          CommunityReportRequest.CreateDTO createDTO){
        // 중복 신고 방지
        if(reportRepository.existsByReporterIdAndPostId(memberId,postId)){
            throw new Exception400("이미 신고한 게시글 입니다.");
        }

        Member reporter = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));

        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        CommunityReport report = createDTO.toEntity(post, reporter);
        reportRepository.save(report);

        return new CommunityReportResponse.CreateDTO(report, "신고가 접수되었습니다");
    }

    // 전체 조회
    public List<CommunityReportResponse.ListDTO> findAllMyReports(Long memberId, Pageable pageable){
        Page<CommunityReport> reports = reportRepository.findAllByReporterIdOrderByCreatedAtDesc(memberId, pageable);
        return reports.stream().map(CommunityReportResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 상세조회
    public CommunityReportResponse.DetailDTO detail(Long reportId, Long memberId){
        CommunityReport report = reportRepository.findByIdWithAdminComments(reportId)
                .orElseThrow(() -> new Exception404("신고 내역을 찾을 수 없습니다."));

        if(!report.getReporter().getId().equals(memberId)){
            throw new Exception400("본인이 신고한 내역만 조회할 수 있습니다.");
        }
        return new CommunityReportResponse.DetailDTO(report);
    }
}
