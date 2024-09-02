package com.hh.multiboarduserbackend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class Response<T> {
    private String message;
    private T data;

    /**
     * 메세지 성공 반환 해주는 경우
     * @param message - 메세지
     */
    public Response(String message) {
        this.message = message;
        this.data = null;
    }

    /**
     * 데이터를 포함해서 성공 반환 해주는 경우
     * @param data - 데이터
     */
    public Response(T data) {
        this.message = null;
        this.data = data;
    }

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
