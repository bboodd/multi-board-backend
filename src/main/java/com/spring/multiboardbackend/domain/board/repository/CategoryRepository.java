package com.spring.multiboardbackend.domain.board.repository;

import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryRepository {

    /**
     * 카테고리 단건 조회
     */
    Optional<CategoryVO> findById(Long id);

    /**
     * 게시판 타입별 카테고리 목록 조회
     */
    List<CategoryVO> findAllByBoardTypeId(Long boardTypeId);
}