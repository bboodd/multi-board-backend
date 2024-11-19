package com.spring.multiboardbackend.domain.post.mappers;

import com.spring.multiboardbackend.domain.post.dto.request.PostRequestDto;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponseDto;
import com.spring.multiboardbackend.domain.post.vo.PostVo;
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
