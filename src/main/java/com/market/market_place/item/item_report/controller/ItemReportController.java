package com.market.market_place.item.item_report.controller;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.item.item_report.dto.ItemReportRequest;
import com.market.market_place.item.item_report.dto.ItemReportResponse;
import com.market.market_place.item.item_report.service.ItemReportService;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemReportController {

    private final ItemReportService itemReportService;

    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/{itemId}/reports")
    public ResponseEntity<ItemReportResponse.ItemReportSaveDTO> create(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemReportRequest.ItemReportSaveDTO dto) {

        ItemReportResponse.ItemReportSaveDTO body = itemReportService.save(itemId, sessionUser.getId(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


}
