package com.spring.multiboardbackend.domain.post.dto.response;

import com.spring.multiboardbackend.domain.post.enums.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "파일 응답")
public record FileResponse(
        @Schema(description = "파일 ID", example = "1")
        Long id,

        @Schema(description = "게시글 ID", example = "1")
        Long postId,

        @Schema(description = "원본 파일명", example = "profile.jpg")
        String originalName,

        @Schema(description = "저장된 파일명", example = "abc123def456_profile.jpg")
        String savedName,

        @Schema(description = "저장 경로", example = "/upload/images")
        String savedPath,

        @Schema(description = "파일 크기(byte)", example = "1024")
        Long fileSize,

        @Schema(description = "파일 유형", example = "ATTACHMENT", enumAsRef = true)
        FileType fileType,

        @Schema(description = "컨텐츠 타입", example = "image/jpeg")
        String contentType,

        @Schema(description = "생성일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt
) {
}
