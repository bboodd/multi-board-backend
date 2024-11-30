package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostRepository {

    /**
     * 게시글 저장
     */
    void save(PostVO post);

    /**
     * 게시글 단건 조회
     */
    Optional<PostVO> findById(Long id);

    /**
     * 게시글 상세 조회 (파일, 댓글 포함)
     */
    Optional<PostVO> findByIdWithDetail(Long id);

    /**
     * 게시글 존재 여부 확인
     */
    boolean existsById(Long id);

    /**
     * 게시글 수정
     */
    void update(PostVO post);

    /**
     * 게시글 삭제 (soft delete)
     */
    void deleteById(Long id);     // softDeleteById -> deleteById

    /**
     * 검색 조건에 따른 게시글 목록 조회
     */
    List<PostVO> findAllBySearch(@Param("search") SearchVO search);

    /**
     * 검색 조건에 따른 게시글 수 조회
     */
    int countBySearch(@Param("search") SearchVO search);

    /**
     * 조회수 증가
     */
    void incrementViewCount(Long id);

    /**
     * 게시글 단건 조회 (삭제 여부 무관)
     */
    Optional<PostVO> findByIdWithDeleted(Long id);

}
