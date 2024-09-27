package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.comment.request.CommentRequestDto;
import com.hh.multiboarduserbackend.domain.comment.response.CommentResponseDto;
import com.hh.multiboarduserbackend.domain.comment.CommentVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentVo toVo(CommentRequestDto commentRequestDto, Long memberId, Long postId);

    CommentResponseDto toDto(CommentVo commentVo);

    default List<CommentResponseDto> toDtoList(List<CommentVo> commentVoList) {
        return commentVoList.stream().map(this::toDto).collect(toList());
    }
}
