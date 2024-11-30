package com.spring.multiboardbackend.domain.post.exception;

import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("java:S6548")
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PERMISSION_DENIED("게시물 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);

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
