package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SearchMapper {

    SearchMapper INSTANCE = Mappers.getMapper(SearchMapper.class);

    SearchVo toVo(SearchDto searchDto);

    default SearchVo toVo(SearchDto searchDto, PaginationDto paginationDto) {
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
                .build();
    }
}
