package markit.email.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import markit._core._exception.Exception400;
import markit._core._utils.ApiUtil;
import markit.email.VerificationPurpose;
import markit.email.dtos.*;
import markit.email.services.EmailAccountService;
import markit.email.services.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email API", description = "이메일 인증 관련 API (회원가입, 비밀번호 재설정)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final EmailAccountService emailAccountService;

    // --- 회원가입 --- 
    @Operation(summary = "회원가입용 인증 코드 발송", description = "회원가입을 위해 해당 이메일로 인증 코드를 발송합니다.")
    @PostMapping("/register/send-code")
    public ResponseEntity<ApiUtil.ApiResult<String>> sendRegistrationCode(
            @Valid @RequestBody SendVerificationRequest request
    ) {
        emailAccountService.sendRegistrationCode(request.getEmail());
        return ResponseEntity.ok(ApiUtil.success("인증 코드가 성공적으로 발송되었습니다."));
    }

    @Operation(summary = "회원가입용 인증 코드 확인", description = "발송된 회원가입용 인증 코드가 유효한지 확인합니다.")
    @PostMapping("/register/confirm-code")
    public ResponseEntity<ApiUtil.ApiResult<String>> confirmRegistrationCode(
            @Valid @RequestBody ConfirmVerificationRequest request
    ) {
        boolean isVerified = emailVerificationService.verifyCode(
                request.getEmail(),
                VerificationPurpose.REGISTER,
                request.getCode()
        );
        if (!isVerified) {
            throw new Exception400("인증 코드가 유효하지 않거나 만료되었습니다.");
        }
        return ResponseEntity.ok(ApiUtil.success("이메일 인증이 성공적으로 완료되었습니다."));
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
