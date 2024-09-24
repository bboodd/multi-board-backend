package com.hh.multiboarduserbackend.domain.category;

import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.domain.board.BoardType;
import com.hh.multiboarduserbackend.domain.category.response.CategoryResponseDto;
import com.hh.multiboarduserbackend.mappers.CategoryMapper;
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
    public ResponseEntity<?> getCategories(@PathVariable String boardType) {

        Long typeId = BoardType.getTypeId(boardType);

        List<CategoryVo> categoryVoList = categoryService.findAll(typeId);
        List<CategoryResponseDto> categoryDtoList = categoryModelMapper.toDtoList(categoryVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(categoryDtoList));
    }
}
