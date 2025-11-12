package markit.moderation.sanction.item_sanction;

import markit._core._exception.Exception404;
import markit.item.item_report._enum.ProcessResult;
import markit.item.item_report.entity.ItemReport;
import markit.item.item_report.repository.ItemReportProcessRepository;
import markit.item.item_report.repository.ItemReportRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import markit.moderation.policy.ModerationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSanctionService {

    private final ItemSanctionRepository itemSanctionRepository;
    private final MemberRepository memberRepository;
    private final ItemReportProcessRepository itemReportProcessRepository;
    private final ItemReportRepository itemReportRepository;

    @Transactional
    public ItemSanctionResponse issueSanction(ItemSanctionRequest request) {
        log.info("[issueSanction] request={}", request);

        if (request.getProcessResult() != ProcessResult.ACCEPTED) {
            log.info("[issueSanction] SKIP: processResult != ACCEPTED -> {}", request.getProcessResult());
            return ItemSanctionResponse.none(request.getReportedMemberId());
        }

        Member member = memberRepository.findById(request.getReportedMemberId())
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
        ItemReport report = itemReportRepository.findById(request.getReportId())
                .orElseThrow(() -> new Exception404("해당 신고를 찾을 수 없습니다."));

        long acceptedCountLong = itemReportProcessRepository
                .countByItemReport_Item_Member_IdAndResult(request.getReportedMemberId(), ProcessResult.ACCEPTED);
        int acceptedCount = (int) acceptedCountLong;
        log.info("[issueSanction] acceptedCount={}", acceptedCount);

        if (acceptedCount >= 5) {
            LocalDateTime nowPerm = LocalDateTime.now();
            ItemSanction perm = ItemSanction.builder()
                    .member(member)
                    .report(report)
                    .type(ItemSanctionType.PERM_BAN)
                    .acceptedCountAtCreation(acceptedCount)
                    .reason(request.getAdminReason())
                    .startAt(nowPerm)
                    .active(true)
                    .build();

            ItemSanction saved = itemSanctionRepository.saveAndFlush(perm); // flush 강제
            log.info("[issueSanction] PERM_BAN inserted id={}", saved.getId());

            member.ban();
            return ItemSanctionResponse.from(saved);
        }

        Duration duration = ModerationPolicy.tempBanDurationByAcceptedCount(acceptedCount);
        log.info("[issueSanction] tempBan duration={}", duration);

        if (duration.isZero()) {
            log.info("[issueSanction] SKIP: duration is zero");
            return ItemSanctionResponse.none(request.getReportedMemberId());
        }

        LocalDateTime now = LocalDateTime.now();
        ItemSanction temp = ItemSanction.builder()
                .member(member)
                .report(report)
                .type(ItemSanctionType.TEMP_BAN)
                .acceptedCountAtCreation(acceptedCount)
                .reason(request.getAdminReason())
                .startAt(now)
                .endAt(now.plus(duration))
                .active(true)
                .build();

        ItemSanction saved = itemSanctionRepository.saveAndFlush(temp); // flush 강제
        log.info("[issueSanction] TEMP_BAN inserted id={}", saved.getId());

        member.ban();
        return ItemSanctionResponse.from(saved);
    }

    @Transactional
    public void releaseIfExpired(Long sanctionId) {
        ItemSanction itemSanction = itemSanctionRepository.findById(sanctionId)
                .orElseThrow(() -> new Exception404("해당 제재 내역을 찾을 수 없습니다."));

        if (itemSanction.isActiveNow(LocalDateTime.now())) {
            return;
        }

        itemSanction.deactivate();

        itemSanction.getMember().activate();
    }
}