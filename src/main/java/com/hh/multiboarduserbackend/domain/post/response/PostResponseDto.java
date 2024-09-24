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
        , String categoryName
        , String nickname
        , int fileCnt
        , int commentCnt
        , String thumbnailUrl
        , int answerCnt
        , int lockYn
) {

}
