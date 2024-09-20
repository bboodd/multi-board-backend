package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.request.PostRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.PostResponseDto;
import com.hh.multiboarduserbackend.common.vo.PostVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostResponseDto toDto(PostVo postVo);

    default List<PostResponseDto> toDtoList(List<PostVo> postVoList) {
        return postVoList.stream().map(this::toDto).collect(toList());
    }

    PostVo toVoWithMemberId(PostRequestDto postRequestDto, Long memberId);

}
