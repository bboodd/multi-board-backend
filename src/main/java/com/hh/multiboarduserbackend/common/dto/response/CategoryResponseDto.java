package com.hh.multiboarduserbackend.common.dto.response;

import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import com.hh.multiboarduserbackend.mappers.CategoryMapper;
import lombok.Builder;

@Builder
public record CategoryResponseDto(
          Long categoryId
        , String categoryName
) {

    public static CategoryResponseDto toDto(CategoryVo categoryVo) {
        return CategoryMapper.INSTANCE.toDto(categoryVo);
    }
}
