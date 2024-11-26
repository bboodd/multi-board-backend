package com.spring.multiboardbackend.domain.board.api;

import com.spring.multiboardbackend.domain.board.docs.CategoryControllerDocs;
import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.domain.board.service.CategoryService;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
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

    // 카테고리 목록 조회
    @GetMapping("/{boardType}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(@PathVariable String boardType) {

        BoardType type = BoardType.from(boardType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.findAllByBoardType(type.getId()));
    }
}
