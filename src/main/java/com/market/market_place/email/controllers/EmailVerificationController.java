package com.market.market_place.email.controllers;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place.email.dtos.ConfirmVerificationRequest;
import com.market.market_place.email.dtos.SendVerificationRequest;
import com.market.market_place.email.services.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email API", description = "이메일 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "이메일 인증 코드 발송", description = "회원가입을 위해 해당 이메일로 인증 코드를 발송합니다.")
    @PostMapping("/send-verification")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendVerificationCode(
            @Valid @RequestBody SendVerificationRequest request) {
        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiUtil.success("인증 코드가 성공적으로 발송되었습니다."));
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "발송된 인증 코드가 유효한지 확인합니다.")
    @PostMapping("/confirm-verification")
    public ResponseEntity<ApiUtil.ApiResult<String>> confirmVerificationCode(
            @Valid @RequestBody ConfirmVerificationRequest request) {
        boolean isVerified = emailVerificationService.verifyCode(request.getEmail(), request.getCode());

        if (!isVerified) {
            throw new Exception400("인증 코드가 유효하지 않거나 만료되었습니다.");
        }

        return ResponseEntity.ok(ApiUtil.success("이메일 인증이 성공적으로 완료되었습니다."));
    }
}
