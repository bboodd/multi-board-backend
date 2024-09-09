package com.hh.multiboarduserbackend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
public record Response<T> (
          String message
        , @JsonInclude(JsonInclude.Include.NON_NULL)
          T data
        , boolean valid
) {

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
