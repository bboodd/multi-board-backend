package com.hh.multiboarduserbackend.common.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record SearchDto (
        // TODO: 검색조건 유효성검사
           String startDate
         , String endDate
         , Long categoryId
         , String keyword
         , int page          //현재 페이지 번호
         , int recordSize     //페이지당 출력할 데이터 개수
         , int pageSize     //화면 하단 출력할 페이지 사이즈
) {
    public SearchDto {
        if(Objects.isNull(startDate)) {
            startDate = "";
        }
        if(Objects.isNull(endDate)) {
            endDate = "";
        }
        if(Objects.isNull(categoryId)) {
            categoryId = 0L;
        }
        if(Objects.isNull(keyword)) {
            keyword = "";
        }
        if(page == 0) {
            page = 1;
        }
        if(recordSize == 0) {
            recordSize = 10;
        }
        if(pageSize == 0) {
            pageSize = 10;
        }
    }
}
