package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.mappers.PostMapper;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FreePostResponseDto(
          Long freePostId
        , Long memberId
        , Long freeCategoryId
        , String title
        , String content
        , int viewCnt
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
        , int deleteYn
        , String freeCategoryName   // 서브쿼리문
        , int fileCount             // 서브쿼리문
        , String nickname           // 서브쿼리문
) {

    public static FreePostResponseDto toDto(FreePostVo freePostVo) {
        return PostMapper.INSTANCE.toDto(freePostVo);
    }
}
