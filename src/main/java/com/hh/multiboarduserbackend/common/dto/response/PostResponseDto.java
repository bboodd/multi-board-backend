package com.hh.multiboarduserbackend.common.dto.response;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.mappers.PostMapper;
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
        , int deleteYn
        , String categoryName   // 서브쿼리문
        , String nickname           // 서브쿼리문
        , int fileCount             // 서브쿼리문
) {

    public static PostResponseDto toDto(PostVo postVo) {
        return PostMapper.INSTANCE.toDto(postVo);
    }
}
