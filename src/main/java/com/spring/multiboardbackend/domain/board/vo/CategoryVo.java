package com.spring.multiboardbackend.domain.board.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {

    private Long categoryId;        // pk
    private String categoryName;    // 카테고리 이름
}
