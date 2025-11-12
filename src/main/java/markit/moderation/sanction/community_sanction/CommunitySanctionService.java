package markit.moderation.sanction.community_sanction;

import markit._core._exception.Exception403;
import markit._core._exception.Exception404;
import markit.community.community_report.CommunityReport;
import markit.community.community_report.CommunityReportRepository;
import markit.community.community_report.CommunityReportStatus;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunitySanctionService {

    private final CommunitySanctionRepository communitySanctionRepository;
    private final CommunityReportRepository communityReportRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher publisher;

    public void ensurePostAllowed(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        boolean banned = communitySanctionRepository
                .existsByMember_IdAndActiveTrueAndEndAtAfter(memberId, now);
        if (banned) {
            throw new Exception403("게시글 작성이 제한되어 있습니다.");
        }
    }

    @Transactional
    public CommunitySanctionResponse issueOnReportProcessed(Long memberId, Long reportId, CommunitySanctionRequest request) {

        if (request.getStatus() != CommunityReportStatus.APPROVED) {
            return null;
        }

        if (communitySanctionRepository.existsByReport_Id(reportId)) {
            return CommunitySanctionResponse.from(
                    communitySanctionRepository.findByReport_Id(reportId).orElseThrow()
            );
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾지 못했습니다."));

        CommunityReport report = communityReportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("해당 신고르 찾지 못했습니다."));

        int current = communitySanctionRepository.findFirstByMember_IdOrderByIdDesc(memberId)
                .map(CommunitySanction::getSanctionCount)
                .orElse(0);
        int next = current + 1;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endAt = calcEndAt(next, now);
        boolean active = endAt.isAfter(now);

        CommunitySanction communitySanction = CommunitySanction.builder()
                .member(member)
                .report(report)
                .reason(request.getReason())
                .sanctionCount(next)
                .startAt(now)
                .endAt(endAt)
                .active(active)
                .build();

        communitySanctionRepository.save(communitySanction);
        log.info("[Sanction] memberId={} reportId={} -> 누적 {}회, active={}, endAt={}",
                memberId, reportId, next, active, endAt);

        Long postId = (report.getPost() != null ? report.getPost().getId() : null);
        if (postId != null) {
            publisher.publishEvent(new PostRemovalRequestedEvent(
                    postId,
                    "신고 승인으로 삭제",     // reason
                    reportId,
                    memberId

            ));
        }
        return CommunitySanctionResponse.from(communitySanction);
    }

    @Transactional(readOnly = true)
    public boolean isPostCreationBanned(Long memberId) {
        return communitySanctionRepository.findByMember_IdAndActiveTrueAndEndAtAfter(memberId, LocalDateTime.now())
                .stream().findFirst().isPresent();
    }

    // 제재 횟수별 기간 계산
    private LocalDateTime calcEndAt(int count, LocalDateTime start) {
        return switch (count) {
            case 1, 2 -> start;
            case 3 -> start.plusDays(7);
            case 4 -> start.plusDays(15);
            case 5 -> start.plusDays(30);
            default -> start.plusYears(100);
        };
    }
}
