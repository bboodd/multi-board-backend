package com.hh.multiboarduserbackend.domain.free.category;

import com.hh.multiboarduserbackend.common.dto.response.CategoryResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api-board/free-board")
public class FreeCategoryController {

    private final FreeCategoryService categoryService;

    // 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<Response> getCategories() {

        List<CategoryResponseDto> categoryList = categoryService.findAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(categoryList));
    }
}
