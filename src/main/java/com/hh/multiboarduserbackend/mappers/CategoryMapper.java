package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.category.response.CategoryResponseDto;
import com.hh.multiboarduserbackend.domain.category.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDto toDto(CategoryVo categoryVo);

    default List<CategoryResponseDto> toDtoList(List<CategoryVo> categoryVoList) {
        return categoryVoList.stream().map(this::toDto).collect(toList());
    }
}
