package com.market.market_place._core._exception;

/**
 * 인증은 되었으나, 해당 리소스에 대한 접근 권한이 없는 경우 발생하는 예외 (HTTP 403 Forbidden)
 * <p>
 * 예: 일반 사용자가 관리자 전용 페이지에 접근을 시도하는 경우에 사용됩니다.
 * </p>
 */
public class Exception403 extends RuntimeException {

    private final String errorCode = "FORBIDDEN";

    /**
     * @param message 예외 발생의 원인이 되는 메시지
     */
    public Exception403(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
