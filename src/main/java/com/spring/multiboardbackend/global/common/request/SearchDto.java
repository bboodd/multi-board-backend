package com.spring.multiboardbackend.global.common.request;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

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
         , String nickname
) {
//    pattern = "yyyy-MM-dd`T`HH:mm:ss"
    public SearchDto {
        if(startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        } else {
            startDate = startDate.plusHours(9);
        }
        if(endDate == null) {
            endDate = LocalDateTime.now();
        } else {
            endDate = endDate.plusHours(9);
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
