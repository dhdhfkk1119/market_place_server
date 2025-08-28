package com.market.market_place._core._exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 모든 컨트롤러에서 발생하는 예외를 처리하는 글로벌 예외 핸들러
 */
@RestControllerAdvice
public class MyExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    // 유효성 검사 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        // 첫 번째 에러 메시지를 사용
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<ErrorResponse> ex400(Exception400 e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<ErrorResponse> ex401(Exception401 e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<ErrorResponse> ex403(Exception403 e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<ErrorResponse> ex404(Exception404 e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<ErrorResponse> ex500(Exception500 e) {
        log.error("======================================================");
        log.error("Internal Server Error", e);
        log.error("======================================================");
        ErrorResponse response = new ErrorResponse("서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 그 외 모든 예외를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unknown(Exception e) {
        log.error("======================================================");
        log.error("Unknown Server Error", e);
        log.error("======================================================");
        ErrorResponse response = new ErrorResponse("알 수 없는 서버 오류가 발생했습니다. 관리자에게 문의해주세요.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
