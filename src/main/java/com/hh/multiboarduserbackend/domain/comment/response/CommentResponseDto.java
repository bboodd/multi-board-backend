package com.hh.multiboarduserbackend.domain.comment.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto (
          Long commentId
        , Long postId
        , Long memberId
        , String content
        , LocalDateTime createdDate
        , int deleteYn
        , String nickname
) {

}
