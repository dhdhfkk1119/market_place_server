package markit._core._utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

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

    // 실패 응답을 생성하는 정적 메서드 (유효성 검사 오류 포함)
    public static <T> ApiResult<T> fail(String errorMessage, HttpStatus status, String errorCode, Map<String, String> validationErrors) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, status.value(), errorCode, validationErrors));
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
        @JsonInclude(JsonInclude.Include.NON_NULL) // code가 null이면 JSON에서 제외
        private final String code;
        @JsonInclude(JsonInclude.Include.NON_NULL) // validationErrors가 null이면 JSON에서 제외
        private final Map<String, String> validationErrors;

        // 기존 생성자
        private ApiError(String message, int status) {
            this(message, status, null, null);
        }

        // 에러 코드를 받는 새로운 생성자
        private ApiError(String message, int status, String code) {
            this(message, status, code, null);
        }

        // 유효성 검사 오류를 포함하는 최종 생성자
        private ApiError(String message, int status, String code, Map<String, String> validationErrors) {
            this.message = message;
            this.status = status;
            this.code = code;
            this.validationErrors = validationErrors;
        }
    }

/*
API 응답 명세

[최근 변경 사항]
- 유효성 검사 실패 시, 어떤 필드에서 어떤 오류가 발생했는지 상세히 알려주는 'validationErrors' 필드가 'error' 객체에 추가되었습니다.

이 클래스는 서버의 모든 API 응답에 대한 표준 구조를 정의합니다.
응답은 항상 'success', 'response', 'error' 세 개의 최상위 필드를 가집니다.

1. 성공 응답 (success: true)
요청이 성공적으로 처리되었을 때의 응답 구조입니다.
'response' 필드에는 각 API의 명세에 따른 실제 데이터가 담기며, 'error' 필드는 null 입니다.

{
  "success": true,
  "response": {
    "id": 1,
    "name": "example data"
    // ... API별 실제 데이터
  },
  "error": null
}


2. 실패 응답 (success: false)
요청 처리 중 오류가 발생했을 때의 응답 구조입니다.
'response' 필드는 null 이며, 'error' 필드에 오류에 대한 상세 정보가 담깁니다.

2.1. 일반적인 실패

{
  "success": false,
  "response": null,
  "error": {
    "message": "오류에 대한 설명 (예: '상품을 찾을 수 없습니다.')",
    "status": 404,
    "code": "RESOURCE_NOT_FOUND"
  }
}

2.2. 유효성 검사 실패
입력값 유효성 검사(@Valid)에 실패했을 경우, 'validationErrors' 필드에 각 필드별 오류 내용이 추가됩니다.
클라이언트는 이 정보를 사용하여 사용자에게 어떤 입력값이 잘못되었는지 알려줄 수 있습니다.

{
  "success": false,
  "response": null,
  "error": {
    "message": "유효성 검사에 실패했습니다.",
    "status": 400,
    "code": "VALIDATION_FAILED",
    "validationErrors": {
      "email": "이메일 형식이 올바르지 않습니다.",
      "password": "비밀번호는 8자 이상이어야 합니다."
    }
  }
}

*/
}
