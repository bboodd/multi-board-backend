package com.hh.multiboarduserbackend.common.vo;

import lombok.Builder;

@Builder
public record CategoryVo(
          Long categoryId
        , String categoryName
) {
}
