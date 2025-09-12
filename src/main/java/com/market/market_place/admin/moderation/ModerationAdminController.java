package com.market.market_place.admin.moderation;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.item.item_report.dto.ItemReportProcessRequest;
import com.market.market_place.item.item_report.dto.ItemReportProcessResponse;
import com.market.market_place.item.item_report.service.ItemReportProcessService;
import com.market.market_place.members.domain.Role;
import com.market.market_place.moderation.sanction.item_sanction.ItemSanctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/moderation")
@RequiredArgsConstructor
public class ModerationAdminController {

    private final ItemReportProcessService itemReportProcessService;
    private final ItemSanctionService itemSanctionService;

    @Auth(roles = {Role.ADMIN})
    @PostMapping("/reports/{reportId}/process")
    public ResponseEntity<ItemReportProcessResponse.ItemReportProcessDetailDTO> processReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ItemReportProcessRequest.ItemReportProcessUpdateDTO req,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {

        ItemReportProcessResponse.ItemReportProcessDetailDTO dto =
                itemReportProcessService.process(reportId, sessionUser.getId(), req);

        return ResponseEntity.ok(dto);
    }
}
