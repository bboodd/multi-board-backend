package com.hh.multiboarduserbackend.domain.category.response;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
          Long categoryId
        , String categoryName
) {

}
