package com.spring.multiboardbackend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "댓글 생성 요청")
public record CommentRequest(
        @Schema(
                description = "댓글 내용",
                example = "좋은 게시글이네요!",
                minLength = 1,
                maxLength = 500
        )
        @NotBlank(message = "댓글을 입력해 주세요")
        String content
) {
}
