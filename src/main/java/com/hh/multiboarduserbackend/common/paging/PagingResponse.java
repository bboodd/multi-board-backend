package com.hh.multiboarduserbackend.common.paging;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PagingResponse<T> (
          List<T> list
        , Pagination pagination
) {

}
