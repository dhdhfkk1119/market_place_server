package com.market.market_place.community.community_report_process;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.community.community_report.CommunityReportRepository;
import com.market.market_place.community.community_report.CommunityReportStatus;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityReportProcessService {

    private final CommunityReportRepository reportRepository;
    private final CommunityReportProcessRepository processRepository;
    private final MemberRepository memberRepository;

    // 신고 상태 처리
    @Transactional
    public CommunityReportProcessResponse.ListDTO updateStatus(Long reportId, Long adminId, CommunityReportProcessRequest.RequestDTO requestDTO){
        CommunityReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("신고내역을 찾을 수 없습니다"));

        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new Exception404("관리자를 찾을 수 없습니다"));

        report.setStatus(requestDTO.getStatus());

        CommunityReportProcess process = processRepository.save(requestDTO.toEntity(report, admin));

        return new CommunityReportProcessResponse.ListDTO(process);
    }

    // 전체 조회
    public List<CommunityReportProcessResponse.ListDTO> findAllReports(Pageable pageable){
        return processRepository.findAll(pageable)
                .stream().map(CommunityReportProcessResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    // 상세 조회
    public CommunityReportProcessResponse.DetailDTO detail(Long reportId){
        CommunityReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("신고 내역을 찾을 수 없습니다"));

        return new CommunityReportProcessResponse.DetailDTO(report);
    }
}
