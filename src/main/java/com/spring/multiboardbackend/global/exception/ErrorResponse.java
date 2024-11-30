package com.spring.multiboardbackend.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ErrorResponse(
        Error error
) {
    public record Error(
            Integer code,
            String message,
            Object errors  // List<ErrorDetail> | Map<String,String> | String 등 다양한 타입 허용
    ) {}

    // 기본적인 에러
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                new Error(
                        errorCode.getStatus().value(),
                        errorCode.getMessage(),
                        null
                )
        );
    }

    // 상세 에러 정보 포함
    public static ErrorResponse of(
            ErrorCode errorCode,
            Object errors
    ) {
        return new ErrorResponse(
                new Error(
                        errorCode.getStatus().value(),
                        errorCode.getMessage(),
                        errors
                )
        );
    }

    // 커스텀 메시지 포함
    public static ErrorResponse of(
            ErrorCode errorCode,
            String message,
            Object errors
    ) {
        return new ErrorResponse(
                new Error(
                        errorCode.getStatus().value(),
                        message,
                        errors
                )
        );
    }


}
