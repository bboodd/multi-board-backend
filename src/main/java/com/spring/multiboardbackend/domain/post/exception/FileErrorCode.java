package com.spring.multiboardbackend.domain.post.exception;

import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {

    FILE_NOT_FOUND("파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_NOT_IMAGE("이미지 파일이 아닙니다.", HttpStatus.BAD_REQUEST);

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
