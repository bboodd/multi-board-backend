package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.response.CategoryResponseDto;
import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDto toDto(CategoryVo categoryVo);

    default List<CategoryResponseDto> toDtoList(List<CategoryVo> categoryVoList) {
        return categoryVoList.stream().map(this::toDto).collect(toList());
    }
}
