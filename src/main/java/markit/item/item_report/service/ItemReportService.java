package markit.item.item_report.service;

import markit._core._exception.Exception404;
import markit.item.core.Item;
import markit.item.core.ItemRepository;
import markit.item.item_report.dto.ItemReportRequest;
import markit.item.item_report.dto.ItemReportResponse;
import markit.item.item_report.entity.ItemReport;
import markit.item.item_report.entity.ItemReportProcess;
import markit.item.item_report.repository.ItemReportProcessRepository;
import markit.item.item_report.repository.ItemReportRepository;
import markit.members.domain.Member;
import markit.members.repositories.MemberRepository;
import markit.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ItemReportService {

    private final ItemReportRepository itemReportRepository;
    private final ItemReportProcessRepository itemReportProcessRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public ItemReportResponse.ItemReportSaveDTO save(Long itemId, Long reporterId, ItemReportRequest.ItemReportSaveDTO dto) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new Exception404("해당 게시물을 찾을 수 없습니다"));
        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다."));


        ItemReport itemReport = dto.toEntity(item, reporter);
        ItemReport saved = itemReportRepository.save(itemReport);

        return new ItemReportResponse.ItemReportSaveDTO(saved);
    }

    public ItemReport getItemReport(Long reportId) {
        return itemReportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404("해당 신고를 찾을 수 없습니다.")
                );
    }

    @Transactional(readOnly = true)
    public Page<ItemReportResponse.ItemReportListDTO> getMyReports(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return itemReportRepository.findByReporter_Id(memberId, pageable)
                .map(itemReport -> ItemReportResponse.ItemReportListDTO.builder()
                        .id(itemReport.getId())
                        .itemId(itemReport.getItem().getId())
                        .reason(itemReport.getReason())
                        .status(itemReport.getStatus())
                        .createdAt(itemReport.getTime())
                        .itemThumbnail(itemReport.getItem().getThumbnailUrl())
                        .build()
                );
    }

    public ItemReportResponse.ItemReportDetailDTO getReportDetail(Long reportId, Long memberId) {

        ItemReport report = itemReportRepository.findByIdAndReporter_Id(reportId, memberId)
                .orElseThrow(() -> new Exception404("해당 신고를 찾을 수 없습니다."));

        return ItemReportResponse.ItemReportDetailDTO.from(report);
    }

    public ItemReportResponse.ItemReportResultDTO getProcessResult(Long reportId, Long memberId) {

        ItemReport report = getItemReport(reportId);

        if (!report.getReporter().getId().equals(memberId))
            throw new Exception404("본인이 신고한 것만 확인 할 수 있습니다.");

        ItemReportProcess process = itemReportProcessRepository.findByItemReportId(reportId).orElseThrow(
                () -> new Exception404("신고 처리 내역을 찾을 수 없습니다.")
        );

        return ItemReportResponse.ItemReportResultDTO.builder()
                .id(report.getId())
                .result(process.getResult())
                .adminReason(process.getReason())
                .processDate(process.getProcessDate())
                .build();
    }
}
