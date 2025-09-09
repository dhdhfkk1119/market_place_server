package com.market.market_place.community.community_report_process;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class CommunityReportProcessController {

    private final CommunityReportProcessService processService;

    // 신고 상태 처리
    @Auth(roles = Role.ADMIN)
    @PostMapping("/{reportId}/status")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportProcessResponse.ListDTO>> updateStatus(
            @PathVariable Long reportId,
            @RequestBody CommunityReportProcessRequest.RequestDTO requestDTO,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityReportProcessResponse.ListDTO response = processService.updateStatus(reportId, sessionUser.getId(), requestDTO);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 전체 조회
    @Auth(roles = Role.ADMIN)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityReportProcessResponse.ListDTO>>> list(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        List<CommunityReportProcessResponse.ListDTO> reports = processService.findAllReports(pageable);
        return ResponseEntity.ok(ApiUtil.success(reports));
    }

    // 상세 조회
    @Auth(roles = Role.ADMIN)
    @GetMapping("/{reportId}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportProcessResponse.DetailDTO>> detail(
            @PathVariable Long reportId){
        CommunityReportProcessResponse.DetailDTO report = processService.detail(reportId);
        return ResponseEntity.ok(ApiUtil.success(report));
    }
}
