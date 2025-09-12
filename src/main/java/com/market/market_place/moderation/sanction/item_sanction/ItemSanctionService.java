package com.market.market_place.moderation.sanction.item_sanction;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.item.item_report._enum.ProcessResult;
import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.item.item_report.repository.ItemReportProcessRepository;
import com.market.market_place.item.item_report.repository.ItemReportRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.moderation.policy.ModerationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemSanctionService {

    private final ItemSanctionRepository itemSanctionRepository;
    private final MemberRepository memberRepository;
    private final ItemReportProcessRepository itemReportProcessRepository;
    private final ItemReportRepository itemReportRepository;


    @Transactional
    public ItemSanctionResponse issueSanction(ItemSanctionRequest request) {

        if (request.getProcessResult() != ProcessResult.ACCEPTED) {
            return ItemSanctionResponse.none(request.getReportedMemberId());
        }

        Member member = memberRepository.findById(request.getReportedMemberId())
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));

        ItemReport report = itemReportRepository.findById(request.getReportId())
                .orElseThrow(() -> new Exception404("해당 신고를 찾을 수 없습니다."));

        long acceptedCountLong = itemReportProcessRepository
                .countByItemReport_Item_Member_IdAndResult(request.getReportedMemberId(), ProcessResult.ACCEPTED);
        int acceptedCount = (int) acceptedCountLong;

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

            itemSanctionRepository.save(perm);

            member.ban();

            return ItemSanctionResponse.from(perm);
        }

        Duration duration = ModerationPolicy.tempBanDurationByAcceptedCount(acceptedCount);
        if (duration.isZero()) {
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

        itemSanctionRepository.save(temp);

        member.ban();

        return ItemSanctionResponse.from(temp);
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