package com.market.market_place.item.item_report.controller;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.item.item_report.dto.ItemReportProcessRequest;
import com.market.market_place.item.item_report.dto.ItemReportProcessResponse;
import com.market.market_place.item.item_report.service.ItemReportProcessService;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class ItemReportProcessController {

    private final ItemReportProcessService itemReportProcessService;

    @Auth(roles = {Role.ADMIN})
    @PostMapping("/{reportId}/process")
    public ResponseEntity<ItemReportProcessResponse.ItemReportProcessResponseDetailDTO> processReport(
            @PathVariable Long reportId,
            @RequestBody ItemReportProcessRequest.ItemReportProcessRequestUpdateDTO req,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemReportProcessResponse.ItemReportProcessResponseDetailDTO response =
                itemReportProcessService.process(reportId, sessionUser.getId(), req);
        return ResponseEntity.ok(response);
    }

}
