package com.spring.multiboardbackend.domain.post.mapper;

import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentVO toVO(CommentRequest commentRequest, Long memberId, Long postId);

    CommentResponse toResponse(CommentVO commentVO);

    List<CommentResponse> toResponseList(List<CommentVO> comments);
}
