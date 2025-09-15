package com.market.market_place._core._exception;

/**
 * 클라이언트의 잘못된 요청으로 인해 발생하는 예외 (HTTP 400 Bad Request)
 * <p>
 * 주로 유효성 검사(Validation) 실패, 필수 파라미터 누락 등 클라이언트 측의 원인으로 요청을 처리할 수 없을 때 사용됩니다.
 * </p>
 */
public class Exception400 extends RuntimeException {

    private final String errorCode = "BAD_REQUEST";

    /**
     * @param message 예외 발생의 원인이 되는 메시지
     */
    public Exception400(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
