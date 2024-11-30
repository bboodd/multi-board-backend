package com.spring.multiboardbackend.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final transient ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // 서버 에러용 정적 팩토리 메서드
    public static CustomException internalServerError(Throwable cause) {
        return new CustomException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                cause
        );
    }
}
