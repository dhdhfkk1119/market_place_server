package markit.community.community_report;

import markit._core._utils.ApiUtil;
import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/reports")
public class CommunityReportController {

    private final CommunityReportService reportService;

    @Auth(roles = Role.USER)
    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportResponse.CreateDTO>> reportPost(
            @PathVariable Long postId,
            @RequestBody CommunityReportRequest.CreateDTO createDTO,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityReportResponse.CreateDTO response = reportService.createReport(postId, sessionUser.getId(), createDTO);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = Role.USER)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityReportResponse.ListDTO>>> list(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        List<CommunityReportResponse.ListDTO> reports = reportService.findAllMyReports(sessionUser.getId(), pageable);
        return ResponseEntity.ok(ApiUtil.success(reports));
    }

    @Auth(roles = Role.USER)
    @GetMapping("/{reportId}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportResponse.DetailDTO>> detail(
            @PathVariable Long reportId,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityReportResponse.DetailDTO reportDetail = reportService.detail(reportId, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(reportDetail));
    }
}
