package com.market.market_place.members.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role; // 독립된 Role을 import
import com.market.market_place.members.dto_auth.*;
import com.market.market_place.members.dto_profile.MemberUpdateRequest;
import com.market.market_place.members.dto_profile.MemberUpdateResponse;
import com.market.market_place.members.dto_profile.MyInfoResponse;
import com.market.market_place.members.dto_token.AccessTokenResponse;
import com.market.market_place.members.dto_token.LoginResponseWithTokens;
import com.market.market_place.members.dto_token.TokenReissueResponse;
import com.market.market_place.members.services.MemberAdminService;
import com.market.market_place.members.services.MemberAuthService;
import com.market.market_place.members.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberAuthService memberAuthService;
    private final MemberAdminService memberAdminService;

    // --- User / Admin 공통 API ---
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping("/me")
    public ResponseEntity<ApiUtil.ApiResult<MyInfoResponse>> getMyInfo(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        MyInfoResponse response = memberService.getMyInfo(sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "비밀번호 변경", description = "현재 로그인한 사용자의 비밀번호를 변경합니다.")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PatchMapping("/me/password")
    public ResponseEntity<ApiUtil.ApiResult<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        memberAuthService.changePassword(sessionUser.getId(), request);
        return ResponseEntity.ok(ApiUtil.success("비밀번호가 성공적으로 변경되었습니다."));
    }

    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 닉네임 또는 프로필 이미지를 수정합니다.")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PatchMapping("/me")
    public ResponseEntity<ApiUtil.ApiResult<MemberUpdateResponse>> updateMember(
            @Valid @RequestBody MemberUpdateRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        MemberUpdateResponse response = memberService.updateMember(sessionUser.getId(), request);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "회원 탈퇴 (논리적 삭제)", description = "현재 로그인한 사용자가 탈퇴 처리합니다. 데이터는 보존됩니다.")
    @SecurityRequirement(name = "jwtAuth")
    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/me")
    public ResponseEntity<ApiUtil.ApiResult<String>> withdrawMember(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        memberService.withdrawMember(sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success("회원 탈퇴가 정상적으로 처리되었습니다."));
    }

    // --- 인증 API (비로그인) ---
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberAuthService.registerMember(request);
        return new ResponseEntity<>(ApiUtil.success(response), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "로그인 ID와 비밀번호로 인증하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiUtil.ApiResult<MemberLoginResponse>> login(@RequestBody MemberLoginRequest request) {
        LoginResponseWithTokens result = memberAuthService.login(request); // 반환 타입 변경

        // 리프레시 토큰을 HttpOnly 쿠키에 담아 반환
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(true) // HTTPS 사용 시 true
                .path("/")
                .maxAge(result.getRefreshTokenMaxAgeSeconds()) // DTO에서 받은 maxAge 사용
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + result.getAccessToken())
                .body(ApiUtil.success(result.getLoginResponse()));
    }

    @Operation(summary = "토큰 재발급", description = "만료된 액세스 토큰을 리프레시 토큰으로 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiUtil.ApiResult<AccessTokenResponse>> reissueTokens(
            @CookieValue(name = "refreshToken") String refreshToken) {

        TokenReissueResponse response = memberAuthService.reissueTokens(refreshToken);

        // 새로운 리프레시 토큰을 HttpOnly 쿠키에 담아 반환
        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refreshToken", response.getNewRefreshToken())
                .httpOnly(true)
                .secure(true) // HTTPS 사용 시 true
                .path("/")
                .maxAge(response.getRefreshTokenMaxAgeSeconds()) // DTO에서 받은 maxAge 사용
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                .body(ApiUtil.success(new AccessTokenResponse(response.getNewAccessToken())));
    }

    // --- 관리자(Admin) 전용 API ---
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

    // --- Health Check API ---
    @Operation(summary = "API 상태 확인", description = "Member API 서버의 상태를 확인합니다.")
    @GetMapping("/health")
    public ResponseEntity<ApiUtil.ApiResult<String>> healthCheck() {
        return ResponseEntity.ok(ApiUtil.success("Member API is healthy!"));
    }
}
