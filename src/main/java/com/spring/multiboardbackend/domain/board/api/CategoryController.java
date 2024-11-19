package com.spring.multiboardbackend.domain.board.api;

import com.spring.multiboardbackend.domain.board.service.CategoryService;
import com.spring.multiboardbackend.domain.board.vo.CategoryVo;
import com.spring.multiboardbackend.global.common.response.Response;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponseDto;
import com.spring.multiboardbackend.domain.board.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryModelMapper = Mappers.getMapper(CategoryMapper.class);

    // 카테고리 목록 조회
    @GetMapping("/{boardType}/categories")
    public ResponseEntity<Response> getCategories(@PathVariable String boardType) {

        BoardType type = BoardType.from(boardType);

        List<CategoryVo> categoryVoList = categoryService.findAll(type.getId());
        List<CategoryResponseDto> categoryDtoList = categoryModelMapper.toDtoList(categoryVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(categoryDtoList));
    }
}
