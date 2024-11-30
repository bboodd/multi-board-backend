package com.spring.multiboardbackend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일 응답")
public record FileResponse(
        @Schema(description = "파일 ID", example = "1")
        Long id,

        @Schema(description = "원본 파일명", example = "profile.jpg")
        String originalName,

        @Schema(description = "파일 크기(byte)", example = "1024")
        Long fileSize
) {
}
