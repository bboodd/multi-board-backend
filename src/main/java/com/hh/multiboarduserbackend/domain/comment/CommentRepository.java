package com.hh.multiboarduserbackend.domain.comment;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentRepository {

    void save(CommentVo commentVo);

    List<CommentVo> findAllByPostId(Long postId);

    void deleteById(Long commentId);
}
