package com.hh.multiboarduserbackend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
public record Response<T> (
          String message
        , @JsonInclude(JsonInclude.Include.NON_NULL) T data
) {

}
