package com.hh.multiboarduserbackend.common.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryVo {

    private Long categoryId;        // pk
    private String categoryName;    // 카테고리 이름
}
