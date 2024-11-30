package com.spring.multiboardbackend.global.common.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchVO {
    // 검색 조건
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private String keyword;

    // 페이징
    private int page;
    private int size;
    private int offset;

    // 정렬
    private String orderBy;
    private String sortBy;

    // 추가 조건
    private String nickname;
    private Long boardTypeId;

    // 페이징 관련 유틸리티 메서드
    public int getOffset() {
        return (page - 1) * size;
    }
}
