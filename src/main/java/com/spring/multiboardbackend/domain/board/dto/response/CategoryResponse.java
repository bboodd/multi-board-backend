package com.spring.multiboardbackend.domain.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답 DTO")
public record CategoryResponse(
        @Schema(description = "카테고리 ID", example = "1")
        Long id,
        @Schema(description = "카테고리 이름", example = "공지사항")
        String name,
        @Schema(description = "게시판 타입 ID", example = "1")
        Long boardTypeId
) {

}
