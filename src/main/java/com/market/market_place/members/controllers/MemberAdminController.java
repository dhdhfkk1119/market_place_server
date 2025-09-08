package com.market.market_place.members.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.dto_auth.MemberRegisterResponse;
import com.market.market_place.members.services.MemberAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member API 관리자용", description = "관리자 회원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    @Operation(summary = "회원 정지 (관리자용)", description = "특정 회원의 상태를 '정지'로 변경합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = Role.ADMIN)
    @PatchMapping("/{id}/ban")
    public ResponseEntity<ApiUtil.ApiResult<String>> banMember(@PathVariable Long id) {
        memberAdminService.banMember(id);
        return ResponseEntity.ok(ApiUtil.success("해당 회원이 정지 처리되었습니다."));
    }

    @Operation(summary = "회원 상세 조회 (관리자용)", description = "로그인 ID로 특정 회원의 정보를 조회합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = Role.ADMIN)
    @GetMapping("/{loginId}")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> getMember(@PathVariable String loginId) {
        MemberRegisterResponse response = memberAdminService.findMemberByLoginId(loginId);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 전체 조회 (관리자용)", description = "모든 회원의 목록을 조회합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = Role.ADMIN)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<MemberRegisterResponse>>> getAllMembers() {
        List<MemberRegisterResponse> response = memberAdminService.findAllMembers();
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 삭제 (관리자용, 물리적 삭제)", description = "특정 회원의 정보를 시스템에서 영구적으로 삭제합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = Role.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberAdminService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
