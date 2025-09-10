package com.market.market_place.item.item_report.controller;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.item.item_report.dto.ItemReportProcessRequest;
import com.market.market_place.item.item_report.dto.ItemReportProcessResponse;
import com.market.market_place.item.item_report.service.ItemReportProcessService;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class ItemReportProcessController {

    private final ItemReportProcessService itemReportProcessService;

    @Auth(roles = {Role.ADMIN})
    @PostMapping("/{reportId}/process")
    public ResponseEntity<ItemReportProcessResponse.ItemReportProcessDetailDTO> processReport(
            @PathVariable Long reportId,
            @RequestBody ItemReportProcessRequest.ItemReportProcessUpdateDTO req,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemReportProcessResponse.ItemReportProcessDetailDTO response =
                itemReportProcessService.process(reportId, sessionUser.getId(), req);
        return ResponseEntity.ok(response);
    }

    @Auth(roles = {Role.ADMIN})
    @GetMapping
    public ResponseEntity<Page<ItemReportProcessResponse.ItemReportProcessListDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "processDate,desc") String sort,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser
            )

    {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort s = Sort.by(direction, sortParams[0]);

        Pageable pageable = PageRequest.of(page, size, s);
        Page<ItemReportProcessResponse.ItemReportProcessListDTO> itemReportProcessList = itemReportProcessService.findAll(pageable,sessionUser.getId());
        return ResponseEntity.ok(itemReportProcessList);
    }
}
