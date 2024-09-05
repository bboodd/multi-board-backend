package com.hh.multiboarduserbackend.common.vo;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.mappers.SearchMapper;
import lombok.*;

@Builder
public record SearchVo (
            String startDate
          , String endDate
          , Long categoryId
          , String keyword
          , int page                //현재 페이지 번호
          , int recordSize          //페이지당 출력할 데이터 개수
          , int pageSize            //화면 하단 출력할 페이지 사이즈
          , int limitStart
) {

    public static SearchVo toVo(SearchDto searchDto) {
        return SearchMapper.INSTANCE.toVo(searchDto);
    }

    public static SearchVo toVo(SearchDto searchDto, PaginationDto paginationDto) {
        return SearchMapper.INSTANCE.toVo(searchDto, paginationDto);
    }
}
