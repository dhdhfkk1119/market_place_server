package com.market.market_place._core._utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

// 공통 API 응답 형식을 위한 유틸리티 클래스
public class ApiUtil<T> {

    // 성공 응답을 생성하는 정적 메서드
    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    // 실패 응답을 생성하는 정적 메서드 (메시지와 상태 코드만 포함)
    public static <T> ApiResult<T> fail(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, status.value()));
    }

    // 실패 응답을 생성하는 정적 메서드 (메시지, 상태 코드, 에러 코드 포함)
    public static <T> ApiResult<T> fail(String errorMessage, HttpStatus status, String errorCode) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, status.value(), errorCode));
    }

    @Getter @Setter
    public static class ApiResult<T> {
        private final boolean success;
        private final T response;
        private final ApiError error;

        private ApiResult(boolean success, T response, ApiError error) {
            this.success = success;
            this.response = response;
            this.error = error;
        }
    }

    @Getter @Setter
    public static class ApiError {
        private final String message;
        private final int status;
        private final String code; // 에러 코드를 나타내는 필드

        // 기존 생성자
        private ApiError(String message, int status) {
            this(message, status, null); // 에러 코드가 없는 경우
        }

        // 에러 코드를 받는 새로운 생성자
        private ApiError(String message, int status, String code) {
            this.message = message;
            this.status = status;
            this.code = code;
        }
    }
}
