package com.hh.multiboarduserbackend.common.utils;

import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtils {

    private int totalRecordCount;   // 전체 데이터 수
    private int totalPageCount;     // 전체 페이지 수
    private int startPage;          // 첫 페이지 번호
    private int endPage;            // 끝 페이지 번호
    private int limitStart;         // limit 시작 위치
    private boolean existPrevPage;  // 이전 페이지 존재 여부
    private boolean existNextPage;  // 다음 페이지 존재 여부

    public PaginationDto createPagination(int totalRecordCount, SearchDto searchDto) {
        if(totalRecordCount > 0) {
            this.totalRecordCount = totalRecordCount;
            calculation(searchDto);
        }

        return PaginationDto.builder()
                .totalPageCount(totalPageCount)
                .totalRecordCount(totalRecordCount)
                .startPage(startPage)
                .endPage(endPage)
                .limitStart(limitStart)
                .existNextPage(existNextPage)
                .existPrevPage(existPrevPage)
                .build();
    }

    private void calculation(SearchDto searchDto) {

        // 전체 페이지 수 계산
        totalPageCount = ((totalRecordCount - 1) / searchDto.recordSize()) + 1;

        // 첫 페이지 번호 계산
        startPage = ((searchDto.page() -1) / searchDto.pageSize()) * searchDto.pageSize() + 1;

        // 끝 페이지 번호 계산
        endPage = startPage + searchDto.pageSize() - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 전체 페이지 수 저장
        if(endPage > totalPageCount) {
            endPage = totalPageCount;
        }

        // LIMIT 시작 위치 계산
        limitStart = (searchDto.page() - 1) * searchDto.recordSize();

        // 이전 페이지 존재 여부 확인
        existPrevPage = startPage != 1;

        // 다음 페이지 존재 여부 확인
        existNextPage = (endPage * searchDto.recordSize()) < totalRecordCount;
    }
}
