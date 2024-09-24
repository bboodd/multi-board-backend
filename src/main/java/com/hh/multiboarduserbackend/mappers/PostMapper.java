package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.post.request.PostRequestDto;
import com.hh.multiboarduserbackend.domain.post.response.PostResponseDto;
import com.hh.multiboarduserbackend.domain.post.PostVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostResponseDto toDto(PostVo postVo);

    default List<PostResponseDto> toDtoList(List<PostVo> postVoList) {
        return postVoList.stream().map(this::toDto).collect(toList());
    }

    PostVo toVoWithMemberIdAndTypeId(PostRequestDto postRequestDto, Long memberId, Long typeId);

    PostVo toVoWithMemberId(PostRequestDto postRequestDto, Long memberId);

}
