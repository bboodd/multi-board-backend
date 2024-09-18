package com.hh.multiboarduserbackend.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    REQUIRED_ACCESS_TOKEN("엑세스 토큰이 필요한 요청입니다.", HttpStatus.UNAUTHORIZED),
    RESOLVE_TOKEN_ERROR("토큰을 변환할 수 없습니다.", HttpStatus.UNAUTHORIZED),
    NOT_AUTH_TOKEN_ERROR("AUTH TOKEN 이 아닙니다.", HttpStatus.UNAUTHORIZED),
    WRONG_ACCESS_TOKEN("잘못된 토큰 입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("만료된 토큰 입니다.", HttpStatus.UNAUTHORIZED);

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
