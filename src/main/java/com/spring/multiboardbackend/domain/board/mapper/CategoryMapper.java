package com.spring.multiboardbackend.domain.board.mapper;

import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    CategoryResponse toResponse(CategoryVO categoryVo);

    List<CategoryResponse> toResponseList(List<CategoryVO> categoryVOList);
}
