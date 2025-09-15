package com.market.market_place._core._exception;

/**
 * 서버 내부의 예상치 못한 문제로 인해 요청을 처리할 수 없을 때 발생하는 예외 (HTTP 500 Internal Server Error)
 * <p>
 * 예: 데이터베이스 연결 실패, 외부 API 연동 오류 등 서버 측의 로직 문제로 인해 발생합니다.
 * </p>
 */
public class Exception500 extends RuntimeException {

    private final String errorCode = "INTERNAL_SERVER_ERROR";

    /**
     * @param message 예외 발생의 원인이 되는 메시지
     */
    public Exception500(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
