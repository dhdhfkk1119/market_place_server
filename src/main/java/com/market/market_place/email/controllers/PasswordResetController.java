package com.market.market_place.email.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place.email.dtos.ConfirmVerificationRequest;
import com.market.market_place.email.dtos.PasswordResetRequest;
import com.market.market_place.email.dtos.PasswordResetTokenResponse;
import com.market.market_place.email.dtos.SendPasswordResetCodeRequest;
import com.market.market_place.members.services.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Password Reset API", description = "비밀번호 찾기(재설정) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/password-reset")
public class PasswordResetController {

    private final MemberAuthService memberAuthService;

    @Operation(summary = "비밀번호 재설정 코드 발송", description = "아이디와 이메일 정보가 일치하는 회원에게 인증 코드를 발송합니다.")
    @PostMapping("/send-code")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendPasswordResetCode(
            @Valid @RequestBody SendPasswordResetCodeRequest request) {
        memberAuthService.sendPasswordResetCode(request);
        return ResponseEntity.ok(ApiUtil.success("인증 코드가 성공적으로 발송되었습니다."));
    }

    @Operation(summary = "비밀번호 재설정 코드 확인", description = "발송된 인증 코드를 검증하고, 성공 시 비밀번호를 재설정할 수 있는 임시 토큰을 발급합니다.")
    @PostMapping("/confirm-code")
    public ResponseEntity<ApiUtil.ApiResult<PasswordResetTokenResponse>> confirmPasswordResetCode(
            @Valid @RequestBody ConfirmVerificationRequest request) {
        PasswordResetTokenResponse response = memberAuthService.confirmPasswordResetCode(request);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "최종 비밀번호 재설정", description = "발급받은 임시 토큰을 사용하여 최종적으로 비밀번호를 변경합니다.")
    @PostMapping
    public ResponseEntity<ApiUtil.ApiResult<String>> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {
        memberAuthService.resetPassword(request);
        return ResponseEntity.ok(ApiUtil.success("비밀번호가 성공적으로 재설정되었습니다."));
    }
}
