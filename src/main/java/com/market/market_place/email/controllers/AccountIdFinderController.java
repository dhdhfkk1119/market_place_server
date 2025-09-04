package com.market.market_place.email.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place.email.dtos.ConfirmVerificationRequest;
import com.market.market_place.email.dtos.FindIdResponse;
import com.market.market_place.email.dtos.SendVerificationRequest;
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

@Tag(name = "Account Finder API", description = "아이디/비밀번호 찾기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login-id")
public class AccountIdFinderController {

    private final MemberAuthService memberAuthService;

    @Operation(summary = "아이디 찾기용 인증 코드 발송", description = "가입된 이메일로 아이디를 찾기 위한 인증 코드를 발송합니다.")
    @PostMapping("/send-code")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendFindIdCode(
            @Valid @RequestBody SendVerificationRequest request) {
        memberAuthService.sendFindIdCode(request.getEmail());
        return ResponseEntity.ok(ApiUtil.success("인증 코드가 성공적으로 발송되었습니다."));
    }

    @Operation(summary = "아이디 찾기용 코드 확인", description = "발송된 인증 코드를 검증하고, 성공 시 마스킹 처리된 아이디를 반환합니다.")
    @PostMapping("/confirm")
    public ResponseEntity<ApiUtil.ApiResult<FindIdResponse>> confirmFindIdCode(
            @Valid @RequestBody ConfirmVerificationRequest request) {
        FindIdResponse response = memberAuthService.findLoginIdByEmail(request);
        return ResponseEntity.ok(ApiUtil.success(response));
    }
}
