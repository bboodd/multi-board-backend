package com.spring.multiboardbackend.domain.board.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
          Long categoryId
        , String categoryName
) {

}
