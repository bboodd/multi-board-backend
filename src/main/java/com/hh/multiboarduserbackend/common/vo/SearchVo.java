package com.hh.multiboarduserbackend.common.vo;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.mappers.SearchMapper;
import lombok.*;

@Builder
@Data
public class SearchVo {

    private String startDate;       // 검색 시작날짜
    private String endDate;         // 검색 종료날짜
    private Long categoryId;        // 검색 카테고리 아이디
    private String keyword;         // 검색 키워드
    private int page;               // 현재 페이지 번호
    private int recordSize;         // 페이지당 출력할 데이터 개수
    private int pageSize;           // 화면 하단 출력할 페이지 사이즈
    private int limitStart;         // 쿼리 limit 변수

    public static SearchVo toVo(SearchDto searchDto) {
        return SearchMapper.INSTANCE.toVo(searchDto);
    }

    public static SearchVo toVo(SearchDto searchDto, PaginationDto paginationDto) {
        return SearchMapper.INSTANCE.toVo(searchDto, paginationDto);
    }
}
