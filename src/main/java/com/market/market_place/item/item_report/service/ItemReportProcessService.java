package com.market.market_place.item.item_report.service;

import com.market.market_place.item.item_report.dto.ItemReportProcessRequest;
import com.market.market_place.item.item_report.dto.ItemReportProcessResponse;
import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.item.item_report.entity.ItemReportProcess;
import com.market.market_place.item.item_report.repository.ItemReportProcessRepository;
import com.market.market_place.item.item_report.support.ItemReportPolicy;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemReportProcessService {

    private final ItemReportProcessRepository itemReportProcessRepository;
    private final MemberService memberService;
    private final ItemReportService itemReportService;

    //신고상태처리
    @Transactional
    public ItemReportProcessResponse.ItemReportProcessResponseDetailDTO process(
            Long reportId, Long adminId,
            ItemReportProcessRequest.ItemReportProcessRequestUpdateDTO req) {

        //해당 신고 찾기
        ItemReport itemReport = itemReportService.getItemReport(reportId);

        //관리자 찾기
        Member admin = memberService.findMember(adminId);

        ItemReportProcess process = ItemReportProcess.builder()
                .itemReport(itemReport)
                .member(admin)
                .result(req.getResult())
                .reason(req.getReason())
                .processDate(LocalDateTime.now())
                .build();

        itemReportProcessRepository.save(process);

        itemReport.setStatus(ItemReportPolicy.toStatus(req.getResult()));

        return ItemReportProcessResponse.ItemReportProcessResponseDetailDTO.builder()
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
}
