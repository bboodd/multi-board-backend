package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentRepository {

    /**
     * 댓글 저장
     */
    void save(CommentVO comment);

    /**
     * 댓글 단건 조회
     */
    Optional<CommentVO> findById(Long id);

    List<CommentVO> findAllByPostId(Long postId);

    /**
     * 댓글 삭제 (soft delete)
     */
    void deleteById(Long id);  // softDeleteById -> deleteById로 변경
}
