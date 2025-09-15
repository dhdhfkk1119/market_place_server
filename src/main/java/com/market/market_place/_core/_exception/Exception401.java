package com.market.market_place._core._exception;

/**
 * 인증되지 않은 사용자의 요청에 대한 예외 (HTTP 401 Unauthorized)
 * <p>
 * 로그인이 필요한 기능에 인증 없이 접근하거나, 유효하지 않은 인증 정보(e.g., 잘못된 토큰)를 사용했을 때 발생합니다.
 * </p>
 */
public class Exception401 extends RuntimeException {

    private final String errorCode = "UNAUTHORIZED";

    /**
     * @param message 예외 발생의 원인이 되는 메시지
     */
    public Exception401(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
