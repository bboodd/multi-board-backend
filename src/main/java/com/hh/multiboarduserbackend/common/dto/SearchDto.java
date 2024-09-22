package com.hh.multiboarduserbackend.common.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Builder
public record SearchDto (
        // TODO: 검색조건 유효성검사
           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
           LocalDateTime startDate
         , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
           LocalDateTime endDate
         , Long categoryId
         , String keyword
         , Integer page
         , Integer recordSize
         , Integer pageSize
         , String orderBy
         , String sortBy
         , Long memberId
) {
//    pattern = "yyyy-MM-dd`T`HH:mm:ss"
    public SearchDto {
        if(startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }
        if(endDate == null) {
            endDate = LocalDateTime.now();
        }
        if(categoryId == null) {
            categoryId = 0L;
        }
        if(!StringUtils.hasText(keyword)) {
            keyword = "";
        }
        if(page == null) {
            page = 1;
        }
        if(recordSize == null) {
            recordSize = 10;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        if(orderBy == null) {
            orderBy = "createdDate";
        }
        if(sortBy == null) {
            sortBy = "desc";
        }
    }
}
