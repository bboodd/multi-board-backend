package com.hh.multiboarduserbackend.common.vo;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.Pagination;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class SearchVo {

    private String startDate;
    private String endDate;
    private int categoryId;
    private String keyword;

    private int page;           //현재 페이지 번호
    private int recordSize;     //페이지당 출력할 데이터 개수
    private int pageSize;       //화면 하단 출력할 페이지 사이즈

    private Pagination pagination;

    /**
     * dto를 vo로 변환
     * 화면에서 받아서 db로 넘길때
     * @param searchDto - dto
     * @return searchVo - vo
     */

    public static SearchVo toVo(SearchDto searchDto) {
        SearchVo searchVo = SearchVo.builder()
                .startDate(searchDto.getStartDate())
                .endDate(searchDto.getEndDate())
                .categoryId(searchDto.getCategoryId())
                .keyword(searchDto.getKeyword())
                .page(searchDto.getPage())
                .recordSize(searchDto.getRecordSize())
                .pageSize(searchDto.getPageSize())
                .pagination(searchDto.getPagination())
                .build();
        return searchVo;
    }


    public int getOffset() {        //시작
        return (page - 1) * recordSize;
    }
}
