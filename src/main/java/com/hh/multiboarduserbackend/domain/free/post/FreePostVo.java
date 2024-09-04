package com.hh.multiboarduserbackend.domain.free.post;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FreePostVo(
          Long freePostId
        , Long memberId
        , Long freeCategoryId
        , String title
        , String content
        , int viewCnt
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
        , int deleteYn
) {
}
