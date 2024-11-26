package com.spring.multiboardbackend.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record Pagination(
        @Schema(description = "현재 페이지", example = "1")
        int currentPage,
        @Schema(description = "전체 데이터 수", example = "42")
        int totalCount,
        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasMore
) {
    @Builder
    public Pagination {
        // 유효성 검사
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (totalCount < 0) {
            totalCount = 0;
        }
    }

    /**
     * 페이지 정보 생성
     */
    public static Pagination of(int currentPage, int totalCount, int size) {
        return Pagination.builder()
                .currentPage(currentPage)
                .totalCount(totalCount)
                .hasMore(hasMorePages(currentPage, totalCount, size))
                .build();
    }

    public static Pagination empty() {
        return new Pagination(1, 0, false);
    }

    private static boolean hasMorePages(int currentPage, int totalCount, int size) {
        return (long) currentPage * size < totalCount;
    }

}
