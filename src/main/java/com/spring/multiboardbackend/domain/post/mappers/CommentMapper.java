package com.spring.multiboardbackend.domain.post.mappers;

import com.spring.multiboardbackend.domain.post.dto.request.CommentRequestDto;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponseDto;
import com.spring.multiboardbackend.domain.post.vo.CommentVo;
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
