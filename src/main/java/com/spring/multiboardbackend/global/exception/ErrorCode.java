package com.spring.multiboardbackend.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus getStatus();
    String getMessage();

    // 기본 에러 코드 정의
    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode() {
        @Override
        public String name() {
            return "INTERNAL_SERVER_ERROR";
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        @Override
        public String getMessage() {
            return "서버 오류가 발생했습니다";
        }
    };

    // 편의 메서드
    default CustomException defaultException() {
        return new CustomException(this);
    }

    default CustomException defaultException(Throwable cause) {
        return new CustomException(this, cause);
    }

    default CustomException defaultException(String additionalMessage) {
        return new CustomException(this, getMessage() + " " + additionalMessage);
    }
}
