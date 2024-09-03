package com.hh.multiboarduserbackend.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus defaultHttpStatus();
    String defaultMessage();
    RuntimeException defaultException();
    RuntimeException defaultException(Throwable cause);

    public static final ErrorCode DEFAULT_ERROR_CODE = new ErrorCode() {
        @Override
        public String name() {
            return "SERVER_ERROR";
        }

        @Override
        public HttpStatus defaultHttpStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        @Override
        public String defaultMessage() {
            return "서버 오류";
        }

        @Override
        public RuntimeException defaultException() {
            return new CustomException("SERVER_ERROR");
        }

        @Override
        public RuntimeException defaultException(Throwable cause) {
            return new CustomException("SERVER_ERROR", cause);
        }
    };
}
