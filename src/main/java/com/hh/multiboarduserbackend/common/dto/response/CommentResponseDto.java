package com.hh.multiboarduserbackend.common.dto.response;

import com.hh.multiboarduserbackend.common.vo.CommentVo;
import com.hh.multiboarduserbackend.mappers.CommentMapper;
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
