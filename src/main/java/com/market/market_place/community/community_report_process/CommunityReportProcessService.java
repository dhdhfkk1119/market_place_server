package com.market.market_place.community.community_report_process;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.community.community_report.CommunityReportRepository;
import com.market.market_place.community.community_report.CommunityReportStatus;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.moderation.sanction.community_sanction.CommunitySanctionRequest;
import com.market.market_place.moderation.sanction.community_sanction.CommunitySanctionResponse;
import com.market.market_place.moderation.sanction.community_sanction.CommunitySanctionService;
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

    private final CommunitySanctionService communitySanctionService;

    @Transactional
    public CommunityReportProcessResponse.ListDTO updateStatus(Long reportId, Long adminId, CommunityReportProcessRequest.RequestDTO requestDTO){
        CommunityReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("신고내역을 찾을 수 없습니다"));

        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new Exception404("관리자를 찾을 수 없습니다"));

        report.setStatus(requestDTO.getStatus());

        CommunityReportProcess process = processRepository.save(requestDTO.toEntity(report, admin));

        if (requestDTO.getStatus() == CommunityReportStatus.APPROVED) {

            CommunitySanctionRequest communitySanctionRequest = CommunitySanctionRequest.builder()
                    .status(CommunityReportStatus.APPROVED)
                    .reason(requestDTO.getAdminComment())
                    .build();

            Long reportedMemberId = report.getPost().getMember().getId();

            CommunitySanctionResponse sanctionResponse =
                    communitySanctionService.issueOnReportProcessed(reportedMemberId,reportId,communitySanctionRequest);

        }

        return new CommunityReportProcessResponse.ListDTO(process);
    }

    public List<CommunityReportProcessResponse.ListDTO> findAllReports(Pageable pageable){
        return processRepository.findAllWithPost(pageable)
                .stream().map(CommunityReportProcessResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    public CommunityReportProcessResponse.DetailDTO detail(Long reportId){
        CommunityReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("신고 내역을 찾을 수 없습니다"));

        return new CommunityReportProcessResponse.DetailDTO(report);
    }
}
