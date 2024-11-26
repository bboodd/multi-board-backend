package com.spring.multiboardbackend.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "댓글 응답")
public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 ID", example = "1")
        Long postId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "댓글 내용", example = "좋은 게시글이네요!")
        String content,

        @Schema(description = "작성일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "삭제 여부", example = "false")
        boolean deleted,

        @Schema(description = "작성자 닉네임", example = "홍길동")
        String nickname
) {
}
