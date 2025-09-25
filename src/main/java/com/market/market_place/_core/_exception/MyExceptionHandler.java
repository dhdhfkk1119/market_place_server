package com.market.market_place._core._exception;

import com.market.market_place._core._utils.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 모든 컨트롤러에서 발생하는 예외를 처리하는 글로벌 예외 핸들러
 */
@RestControllerAdvice
public class MyExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    // 유효성 검사 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ApiUtil.fail("유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> ex400(Exception400 e) {
        return new ResponseEntity<>(ApiUtil.fail(e.getMessage(), HttpStatus.BAD_REQUEST, e.getErrorCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> ex401(Exception401 e) {
        return new ResponseEntity<>(ApiUtil.fail(e.getMessage(), HttpStatus.UNAUTHORIZED, e.getErrorCode()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> ex403(Exception403 e) {
        return new ResponseEntity<>(ApiUtil.fail(e.getMessage(), HttpStatus.FORBIDDEN, e.getErrorCode()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> ex404(Exception404 e) {
        return new ResponseEntity<>(ApiUtil.fail(e.getMessage(), HttpStatus.NOT_FOUND, e.getErrorCode()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> ex500(Exception500 e) {
        log.error("======================================================");
        log.error("Internal Server Error", e);
        log.error("======================================================");
        return new ResponseEntity<>(ApiUtil.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 그 외 모든 예외를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiUtil.ApiResult<?>> unknown(Exception e) {
        log.error("======================================================");
        log.error("Unknown Server Error", e);
        log.error("======================================================");
        return new ResponseEntity<>(ApiUtil.fail("알 수 없는 서버 오류가 발생했습니다. 관리자에게 문의해주세요.", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
