package com.spring.multiboardbackend.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T> (
          String message,
          T data,
          Boolean valid,
          Integer status,
          String code,
          Instant timestamp,
          Object cause
) {

    @Builder(toBuilder = true)
    public Response {
        // 기본값 설정
        timestamp = timestamp != null ? timestamp : Instant.now();
        status = status != null ? status : HttpStatus.OK.value();
        code = code != null ? code : "SUCCESS";
    }


    public static Response message(String message) {
        return Response.builder()
                .message(message)
                .build();
    }

    public static<T> Response data(T data) {
        return Response.builder()
                .data(data)
                .build();
    }

    public static Response valid(boolean valid) {
        return Response.builder()
                .valid(valid)
                .build();
    }
}
