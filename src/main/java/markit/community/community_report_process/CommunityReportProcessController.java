package markit.community.community_report_process;

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
@RequestMapping("/api/admin/community/reports")
public class CommunityReportProcessController {

    private final CommunityReportProcessService processService;

    @Auth(roles = Role.ADMIN)
    @PostMapping("/{reportId}/status")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportProcessResponse.ListDTO>> updateStatus(
            @PathVariable Long reportId,
            @RequestBody CommunityReportProcessRequest.RequestDTO requestDTO,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityReportProcessResponse.ListDTO response = processService.updateStatus(reportId, sessionUser.getId(), requestDTO);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = Role.ADMIN)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityReportProcessResponse.ListDTO>>> list(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        List<CommunityReportProcessResponse.ListDTO> reports = processService.findAllReports(pageable);
        return ResponseEntity.ok(ApiUtil.success(reports));
    }

    @Auth(roles = Role.ADMIN)
    @GetMapping("/{reportId}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityReportProcessResponse.DetailDTO>> detail(
            @PathVariable Long reportId){
        CommunityReportProcessResponse.DetailDTO report = processService.detail(reportId);
        return ResponseEntity.ok(ApiUtil.success(report));
    }
}
