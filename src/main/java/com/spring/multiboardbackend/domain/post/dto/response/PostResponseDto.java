package com.spring.multiboardbackend.domain.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponseDto(
          Long postId
        , Long memberId
        , Long categoryId
        , String title
        , String content
        , int viewCnt
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
        , Boolean deleteYn
        , String categoryName
        , String nickname
        , int fileCnt
        , int commentCnt
        , String thumbnailUrl
        , Boolean lockYn
) {

}
