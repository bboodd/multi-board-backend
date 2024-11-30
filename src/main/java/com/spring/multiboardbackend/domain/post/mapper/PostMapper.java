package com.spring.multiboardbackend.domain.post.mapper;

import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {FileMapper.class, CommentMapper.class}
)
public interface PostMapper {

    PostResponse toResponse(PostVO post);

    List<PostResponse> toResponseList(List<PostVO> posts);

    PostVO toVO(PostRequest request, Long memberId, Long boardTypeId);

    PostVO toVOForUpdate(PostRequest request, Long id);

    @Mapping(target = "files", source = "files")
    PostResponse toResponseForCreateAndUpdate(PostVO post, List<FileResponse> files);
}
