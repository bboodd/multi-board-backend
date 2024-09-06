package com.hh.multiboarduserbackend.domain.free.comment;

import com.hh.multiboarduserbackend.common.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FreeCommentRepository {

    void save(CommentVo commentVo);

    List<CommentVo> findAllByPostId(Long freePostId);

    void deleteById(Long freeCommentId);
}
