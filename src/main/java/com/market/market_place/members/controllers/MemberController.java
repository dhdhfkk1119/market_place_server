package com.market.market_place.members.controllers;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Member.MemberRole;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Member API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.registerMember(request);
        return new ResponseEntity<>(ApiUtil.success(response), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "로그인 ID와 비밀번호로 인증하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiUtil.ApiResult<MemberLoginResponse>> login(@RequestBody MemberLoginRequest request) {
        Member member = memberService.login(request);
        String token = JwtUtil.createToken(member);
        MemberLoginResponse response = new MemberLoginResponse(member);

        return ResponseEntity.ok()
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + token)
                .body(ApiUtil.success(response));
    }

    @Operation(summary = "회원 정보 수정", description = "회원의 닉네임 또는 프로필 이미지를 수정합니다. (USER, ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {MemberRole.USER, MemberRole.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<MemberUpdateResponse>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        if (sessionUser.getRole() == MemberRole.USER && !sessionUser.getId().equals(id)) {
            throw new Exception403("본인의 정보만 수정할 수 있습니다.");
        }

        Member updatedMember = memberService.updateMember(id, request);
        MemberUpdateResponse response = new MemberUpdateResponse(updatedMember);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 탈퇴 (논리적 삭제)", description = "회원 상태를 '탈퇴'로 변경합니다. 데이터는 보존됩니다. (USER, ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {MemberRole.USER, MemberRole.ADMIN})
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApiUtil.ApiResult<String>> withdrawMember(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        if (sessionUser.getRole() == MemberRole.USER && !sessionUser.getId().equals(id)) {
            throw new Exception403("본인만 탈퇴할 수 있습니다.");
        }

        memberService.withdrawMember(id);
        return ResponseEntity.ok(ApiUtil.success("회원 탈퇴가 정상적으로 처리되었습니다."));
    }

    @Operation(summary = "회원 정지 (관리자용)", description = "특정 회원의 상태를 '정지'로 변경합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = MemberRole.ADMIN)
    @PatchMapping("/{id}/ban")
    public ResponseEntity<ApiUtil.ApiResult<String>> banMember(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        memberService.banMember(id);
        return ResponseEntity.ok(ApiUtil.success("해당 회원이 정지 처리되었습니다."));
    }

    @Operation(summary = "회원 상세 조회", description = "로그인 ID로 특정 회원의 정보를 조회합니다. (USER, ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {MemberRole.USER, MemberRole.ADMIN})
    @GetMapping("/{loginId}")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> getMember(
            @PathVariable String loginId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        Member member = memberService.findMemberByLoginId(loginId);
        MemberRegisterResponse response = new MemberRegisterResponse(member);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 전체 조회", description = "모든 회원의 목록을 조회합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = MemberRole.ADMIN)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<MemberRegisterResponse>>> getAllMembers(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        List<Member> members = memberService.findAllMembers();
        List<MemberRegisterResponse> response = members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 삭제 (물리적 삭제)", description = "특정 회원의 정보를 시스템에서 영구적으로 삭제합니다. (ADMIN 권한 필요)")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = MemberRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "API 상태 확인", description = "Member API 서버의 상태를 확인합니다.")
    @GetMapping("/health")
    public ResponseEntity<ApiUtil.ApiResult<String>> healthCheck() {
        return ResponseEntity.ok(ApiUtil.success("Member API is healthy!"));
    }
}
