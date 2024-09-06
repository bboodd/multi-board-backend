package com.hh.multiboarduserbackend.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentRequestDto (
          Long commentId
        , Long postId
        , @NotBlank(message = "댓글을 입력해 주세요")
          String content
) {
}