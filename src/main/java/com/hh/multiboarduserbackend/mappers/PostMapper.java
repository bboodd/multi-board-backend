package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.request.PostRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.PostResponseDto;
import com.hh.multiboarduserbackend.common.vo.PostVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponseDto toDto(PostVo postVo);

    PostVo toVo(PostRequestDto postRequestDto, Long memberId);

}
