package com.hh.multiboarduserbackend.common.vo;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.Pagination;
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
          , Pagination pagination
) {

    /**
     * dto를 vo로 변환
     * 화면에서 받아서 db로 넘길때
     * @param searchDto - dto
     * @return searchVo - vo
     */

    public static SearchVo toVo(SearchDto searchDto, Pagination pagination) {
        int page = searchDto.page();
        if(pagination != null) {
            if(searchDto.page() > pagination.getTotalPageCount()) {
                page = pagination.getTotalPageCount();
            }
        }
        SearchVo searchVo = SearchVo.builder()
                .startDate(searchDto.startDate())
                .endDate(searchDto.endDate())
                .categoryId(searchDto.categoryId())
                .keyword(searchDto.keyword())
                .page(page)
                .recordSize(searchDto.recordSize())
                .pageSize(searchDto.pageSize())
                .pagination(pagination)
                .build();
        return searchVo;
    }

}
