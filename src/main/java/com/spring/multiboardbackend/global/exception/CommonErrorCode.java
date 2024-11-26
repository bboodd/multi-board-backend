package com.spring.multiboardbackend.global.exception;

import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST("입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    @Override
    public String getMessage() {  // default 접두사 제거
        return message;
    }

    @Override
    public HttpStatus getStatus() {  // default 접두사 제거
        return status;
    }
}
