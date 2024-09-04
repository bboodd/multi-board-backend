package com.hh.multiboarduserbackend.domain.ask.post;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AskPostVo(
            Long askPostId
          , Long memberId
          , String title
          , String content
          , int viewCnt
          , LocalDateTime createdDate
          , LocalDateTime updatedDate
          , int deleteYn
          , int lockYn
) {
}
