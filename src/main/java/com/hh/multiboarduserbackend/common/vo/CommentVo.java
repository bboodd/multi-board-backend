package com.hh.multiboarduserbackend.common.vo;

import com.hh.multiboarduserbackend.common.dto.request.CommentRequestDto;
import com.hh.multiboarduserbackend.mappers.CommentMapper;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentVo(
          Long commentId
        , Long postId
        , Long memberId
        , String content
        , LocalDateTime createdDate
        , int deleteYn
) {

    public CommentVo toVo(CommentRequestDto commentRequestDto) {
        return CommentMapper.INSTANCE.toVo(commentRequestDto);
    }
}
