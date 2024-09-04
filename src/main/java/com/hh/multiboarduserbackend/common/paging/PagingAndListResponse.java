package com.hh.multiboarduserbackend.common.paging;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PagingAndListResponse<T> (
          List<T> listDto
        , PaginationDto paginationDto
) {

    public PagingAndListResponse {
        listDto = new ArrayList<>();
        paginationDto = null;
    }
}
