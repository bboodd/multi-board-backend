package com.spring.multiboardbackend.global.common.response;

import lombok.Builder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PagingAndListResponse<T> (
          List<T> listDto
        , PaginationDto paginationDto
) {

    public PagingAndListResponse {
        if(CollectionUtils.isEmpty(listDto)) {
            listDto = new ArrayList<>();
        }
    }
}
