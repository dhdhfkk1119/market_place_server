package com.market.market_place.community.community_report;

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
@RequestMapping("api/community/posts/report")
public class CommunityReportController {

    private final CommunityReportService reportService;

    // 등록
    @Auth(roles = Role.USER)
    @PostMapping("save/{id}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportResponse.CreateDTO>> reportPost(
            @PathVariable Long id,
            @RequestBody CommunityReportRequest.CreateDTO createDTO,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityReportResponse.CreateDTO response = reportService.createReport(id, sessionUser.getId(), createDTO);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 전체조회
    @Auth(roles = Role.USER)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityReportResponse.ListDTO>>> list(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        List<CommunityReportResponse.ListDTO> reports = reportService.findAllMyReports(sessionUser.getId(), pageable);
        return ResponseEntity.ok(ApiUtil.success(reports));
    }

    // 상세조회
    @Auth(roles = Role.USER)
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportResponse.DetailDTO>> detail(
            @PathVariable Long id,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityReportResponse.DetailDTO reportDetail = reportService.detail(id, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(reportDetail));
    }
}
