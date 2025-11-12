package markit.email.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import markit._core._utils.ApiUtil;
import markit.email.dtos.*;
import markit.email.services.EmailAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email API - 계정 찾기", description = "아이디 찾기 및 비밀번호 재설정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email/account")
public class EmailAccountFinderController {

    private final EmailAccountService emailAccountService;

    // --- 아이디 찾기 (신규 로직) ---
    @Operation(summary = "마스킹된 아이디 조회", description = "이메일을 전송하여 마스킹된 아이디를 조회합니다.")
    @PostMapping("/find-id/masked")
    public ResponseEntity<ApiUtil.ApiResult<FindIdResponse>> getMaskedId(@Valid @RequestBody SendVerificationRequest request) {
        FindIdResponse response = emailAccountService.getMaskedLoginId(request.getEmail());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "전체 아이디 이메일 발송", description = "이메일로 전체 아이디를 발송합니다.")
    @PostMapping("/find-id/send-email")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendFullIdToEmail(@Valid @RequestBody SendVerificationRequest request) {
        emailAccountService.sendFullLoginIdToEmail(request.getEmail());
        return ResponseEntity.ok(ApiUtil.success("아이디가 이메일로 성공적으로 발송되었습니다."));
    }

    // --- 비밀번호 재설정 ---
    @Operation(summary = "비밀번호 재설정용 인증 코드 발송", description = "비밀번호 재설정을 위해 이메일로 인증코드를 발송합니다.")
    @PostMapping("/password/send-code")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendPasswordResetCode(@Valid @RequestBody SendPasswordResetCodeRequest request) {
        emailAccountService.sendPasswordResetCode(request);
        return ResponseEntity.ok(ApiUtil.success("인증 코드가 성공적으로 발송되었습니다."));
    }

    @Operation(summary = "비밀번호 재설정용 인증 코드 확인", description = "인증코드를 검증하고 비밀번호 재설정용 임시 토큰을 발급합니다.")
    @PostMapping("/password/confirm-code")
    public ResponseEntity<ApiUtil.ApiResult<PasswordResetTokenResponse>> confirmPasswordResetCode(@Valid @RequestBody ConfirmVerificationRequest request) {
        PasswordResetTokenResponse response = emailAccountService.confirmPasswordResetCode(request);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Operation(summary = "비밀번호 재설정", description = "임시 토큰을 사용하여 비밀번호를 최종적으로 재설정합니다.")
    @PostMapping("/password/reset")
    public ResponseEntity<ApiUtil.ApiResult<String>> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        emailAccountService.resetPassword(request);
        return ResponseEntity.ok(ApiUtil.success("비밀번호가 성공적으로 재설정되었습니다."));
    }
}
