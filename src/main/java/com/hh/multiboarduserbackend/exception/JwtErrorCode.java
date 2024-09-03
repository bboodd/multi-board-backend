package com.hh.multiboarduserbackend.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    TOKEN_ERROR("잘못된 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    @Override
    public String defaultMessage() {
        return message;
    }

    @Override
    public HttpStatus defaultHttpStatus() {
        return status;
    }

    @Override
    public CustomException defaultException() {
        return new CustomException(this);
    }

    @Override
    public CustomException defaultException(Throwable cause) {
        return new CustomException(this, cause);
    }
}
