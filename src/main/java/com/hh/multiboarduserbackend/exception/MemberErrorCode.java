package com.hh.multiboarduserbackend.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_LOGIN_ID("중복된 아이디 입니다.", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR("유효한 회원 정보가 아닙니다.", HttpStatus.BAD_REQUEST),
    SIGN_UP_PASSWORD_CHECK_ERROR("입력한 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ID_PASSWORD_EQUALS_ERROR("아이디와 동일한 비밀번호는 쓸 수 없습니다.", HttpStatus.BAD_REQUEST);


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
