package com.spring.multiboardbackend.domain.board.api;

import com.spring.multiboardbackend.domain.board.docs.CategoryControllerDocs;
import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.domain.board.mapper.CategoryMapper;
import com.spring.multiboardbackend.domain.board.service.CategoryService;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/boards")
public class CategoryController implements CategoryControllerDocs {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // 카테고리 목록 조회
    @GetMapping("/{boardType}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(@PathVariable String boardType) {

        BoardType type = BoardType.from(boardType);

        List<CategoryVO> categories = categoryService.findAllByBoardType(type.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryMapper.toResponseList(categories));
    }
}
