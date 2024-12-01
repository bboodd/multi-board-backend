package com.spring.multiboardbackend.domain.board.service;

import com.spring.multiboardbackend.domain.board.exception.BoardErrorCode;
import com.spring.multiboardbackend.domain.board.repository.CategoryRepository;
import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 게시판 타입별 카테고리 목록 조회
     *
     * @param boardTypeId 게시판 타입 ID
     * @return 카테고리 응답 Response 리스트
     */
    public List<CategoryVO> findAllByBoardType(Long boardTypeId) {
        return categoryRepository.findAllByBoardTypeId(boardTypeId);
    }

    /**
     * ID로 카테고리 조회
     *
     * @param id 조회할 카테고리 ID
     * @return 카테고리 응답 Response
     */
    public CategoryVO findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(BoardErrorCode.CATEGORY_NOT_FOUND::defaultException);
    }
}
