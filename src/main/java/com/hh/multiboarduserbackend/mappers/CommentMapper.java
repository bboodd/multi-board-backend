package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.request.CommentRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.CommentResponseDto;
import com.hh.multiboarduserbackend.common.vo.CommentVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentVo toVo(CommentRequestDto commentRequestDto);

    CommentResponseDto toDto(CommentVo commentVo);
}
