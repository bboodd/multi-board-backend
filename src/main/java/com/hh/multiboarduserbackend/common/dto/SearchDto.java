package com.hh.multiboarduserbackend.common.dto;

import lombok.Builder;

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
        startDate = "";
        endDate = "";
        categoryId = 0L;
        keyword = "";
        page = 1;
        recordSize = 10;
        pageSize = 10;
    }
}
