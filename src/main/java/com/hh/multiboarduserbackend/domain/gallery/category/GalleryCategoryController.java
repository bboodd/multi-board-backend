package com.hh.multiboarduserbackend.domain.gallery.category;

import com.hh.multiboarduserbackend.common.dto.response.CategoryResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import com.hh.multiboarduserbackend.domain.free.category.FreeCategoryService;
import com.hh.multiboarduserbackend.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/gallery-board")
public class GalleryCategoryController {

    private final GalleryCategoryService categoryService;
    private final CategoryMapper categoryModelMapper = Mappers.getMapper(CategoryMapper.class);

    // 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {

        List<CategoryVo> categoryVoList = categoryService.findAll();
        List<CategoryResponseDto> categoryDtoList = categoryModelMapper.toDtoList(categoryVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(categoryDtoList));
    }
}

