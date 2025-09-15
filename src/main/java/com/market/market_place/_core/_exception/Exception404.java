package com.market.market_place._core._exception;

/**
 * 요청한 리소스를 서버에서 찾을 수 없을 때 발생하는 예외 (HTTP 404 Not Found)
 * <p>
 * 예: 존재하지 않는 게시글 ID로 조회를 시도하는 경우에 사용됩니다.
 * </p>
 */
public class Exception404 extends RuntimeException {

    private final String errorCode = "NOT_FOUND";

    /**
     * @param message 예외 발생의 원인이 되는 메시지
     */
    public Exception404(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
