package com.market.market_place.item.item_report.controller;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.item.item_report.dto.ItemReportRequest;
import com.market.market_place.item.item_report.dto.ItemReportResponse;
import com.market.market_place.item.item_report.service.ItemReportService;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/item-reports")
@RestController
public class ItemReportController {

    private final ItemReportService itemReportService;

    // 신고 생성
    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/{itemId}")
    public ResponseEntity<ItemReportResponse.ItemReportSaveDTO> create(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemReportRequest.ItemReportSaveDTO dto) {

        ItemReportResponse.ItemReportSaveDTO body = itemReportService.save(itemId, sessionUser.getId(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 내 신고 리스트
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/my")
    public ResponseEntity<Page<ItemReportResponse.ItemReportListDTO>> getMyReports(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ItemReportResponse.ItemReportListDTO> result = itemReportService.getMyReports(sessionUser.getId(), page, size);
        return ResponseEntity.ok(result);
    }

    // 내 신고 상세 조회
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/{reportId}")
    public ResponseEntity<ItemReportResponse.ItemReportDetailDTO> getMyReportDetail(
            @PathVariable Long reportId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {

        ItemReportResponse.ItemReportDetailDTO report = itemReportService.getReportDetail(reportId, sessionUser.getId());

        return ResponseEntity.ok(report);
    }

    // 처리된 내 신고 상세 조회
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/{reportId}/process")
    public ResponseEntity<ItemReportResponse.ItemReportResultDTO> getMyReportProcessResult(
            @PathVariable Long reportId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemReportResponse.ItemReportResultDTO dto =
                itemReportService.getProcessResult(reportId, sessionUser.getId());
        return ResponseEntity.ok(dto);
    }
}
