package com.spring.multiboardbackend.global.common.mappers;

import com.spring.multiboardbackend.global.common.request.SearchDto;
import com.spring.multiboardbackend.global.common.response.PaginationDto;
import com.spring.multiboardbackend.global.common.vo.SearchVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SearchMapper {
    SearchVo toVoWithTypeId(SearchDto searchDto, Long typeId);

    default SearchVo toVoWithPaginationAndTypeId(SearchDto searchDto, PaginationDto paginationDto, Long typeId) {
        int page = searchDto.page();

        if(page > paginationDto.totalPageCount()) {
            page = paginationDto.totalPageCount();
        }

        return SearchVo.builder()
                .startDate(searchDto.startDate())
                .endDate(searchDto.endDate())
                .categoryId(searchDto.categoryId())
                .keyword(searchDto.keyword())
                .page(page)
                .recordSize(searchDto.recordSize())
                .pageSize(searchDto.pageSize())
                .limitStart(paginationDto.limitStart())
                .orderBy(searchDto.orderBy())
                .sortBy(searchDto.sortBy())
                .typeId(typeId)
                .build();
    }
}
