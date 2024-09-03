package com.hh.multiboarduserbackend.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PASSWORD_CHECK_ERROR("입력한 비밀번호가 다릅니다.", HttpStatus.BAD_REQUEST);

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
