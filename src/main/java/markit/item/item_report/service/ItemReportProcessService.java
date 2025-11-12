package markit.item.item_report.service;

import markit.item.item_report.dto.ItemReportProcessRequest;
import markit.item.item_report.dto.ItemReportProcessResponse;
import markit.item.item_report.entity.ItemReport;
import markit.item.item_report.entity.ItemReportProcess;
import markit.item.item_report.repository.ItemReportProcessRepository;
import markit.item.item_report.support.ItemReportPolicy;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import markit.members.services.MemberService;
import markit.moderation.sanction.item_sanction.ItemSanctionRequest;
import markit.moderation.sanction.item_sanction.ItemSanctionResponse;
import markit.moderation.sanction.item_sanction.ItemSanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemReportProcessService {

    private final ItemReportProcessRepository itemReportProcessRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ItemSanctionService itemSanctionService;
    private final ItemReportService itemReportService;

    //신고상태처리
    @Transactional
    public ItemReportProcessResponse.ItemReportProcessDetailDTO process(
            Long reportId, Long adminId,
            ItemReportProcessRequest.ItemReportProcessUpdateDTO req) {

        // 상품에서 해당 신고 유저 찾기
        ItemReport itemReport = itemReportService.getItemReport(reportId);

        // 가해자 신고 찾기
        // Member memberStatus = memberService.findMember(reportId); // 해당 유처 찾기
        Member memberStatus = itemReport.getItem().getMember();
        //관리자 찾기
        Member admin = memberService.findMember(adminId);

        ItemReportProcess process = ItemReportProcess.builder()
                .itemReport(itemReport)
                .member(admin)
                .result(req.getResult())
                .reason(req.getReason())
                .processDate(LocalDateTime.now())
                .build();

        // 상태 변경
        memberStatus.ban(); // 회원 상태를 'BANNED'로 변경
        memberRepository.save(memberStatus);

        itemReportProcessRepository.save(process);

        itemReport.setStatus(ItemReportPolicy.toStatus(req.getResult()));

        Long reportedMemberId = itemReport.getItem().getMember().getId();
        ItemSanctionRequest itemSanctionRequest = ItemSanctionRequest.builder()
                .reportedMemberId(reportedMemberId)
                .reportId(itemReport.getId())
                .processResult(req.getResult())
                .adminReason(req.getReason())
                .build();

        ItemSanctionResponse itemSanctionResponse = itemSanctionService.issueSanction(itemSanctionRequest);

        return ItemReportProcessResponse.ItemReportProcessDetailDTO.builder()
                .processId(process.getId())
                .reportId(itemReport.getId())
                .itemId(itemReport.getItem().getId())
                .adminId(admin.getId())
                .result(process.getResult())
                .reason(process.getReason())
                .processedAt(process.getProcessDate())
                .finalStatus(itemReport.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ItemReportProcessResponse.ItemReportProcessListDTO> findAll(Pageable pageable, Long adminId) {
        memberService.findMember(adminId);
        return itemReportProcessRepository.findAll(pageable)
                .map(itemReportProcess -> ItemReportProcessResponse.ItemReportProcessListDTO.builder()
                        .processId(itemReportProcess.getId())
                        .reportId(itemReportProcess.getItemReport().getId())
                        .itemId(itemReportProcess.getItemReport().getItem().getId())
                        .adminId(itemReportProcess.getMember().getId())
                        .reason(itemReportProcess.getReason())
                        .result(itemReportProcess.getResult())
                        .processedAt(itemReportProcess.getProcessDate())
                        .build());
    }
}
