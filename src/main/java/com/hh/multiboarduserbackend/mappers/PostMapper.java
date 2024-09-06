package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.free.post.FreePostRequestDto;
import com.hh.multiboarduserbackend.domain.free.post.FreePostResponseDto;
import com.hh.multiboarduserbackend.domain.free.post.FreePostVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    FreePostResponseDto toDto(FreePostVo freePostVo);

    FreePostVo toVo(FreePostRequestDto freePostRequestDto, Long memberId);

}
