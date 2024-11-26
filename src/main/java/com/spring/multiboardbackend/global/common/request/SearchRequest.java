package com.spring.multiboardbackend.global.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "검색 조건")
public record SearchRequest(
        @Schema(description = "검색 시작일시")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,

        @Schema(description = "검색 종료일시")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,

        @Schema(description = "카테고리 ID")
        Long categoryId,

        @Schema(description = "검색어")
        String keyword,

        @Schema(description = "페이지 번호", defaultValue = "1")
        Integer page,

        @Schema(description = "페이지당 데이터 수", defaultValue = "10")
        Integer size,

        @Schema(description = "정렬 기준", defaultValue = "createdAt",
                allowableValues = {"createdAt", "viewCount", "commentCount"})
        String orderBy,

        @Schema(description = "정렬 방향", defaultValue = "desc",
                allowableValues = {"asc", "desc"})
        String sortBy,

        @Schema(description = "작성자 닉네임")
        String nickname

) {
    @Builder
    public SearchRequest {
        // 기본값 설정
        startDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(1);
        endDate = endDate != null ? endDate : LocalDateTime.now();
        categoryId = categoryId != null ? categoryId : 0L;
        keyword = keyword != null ? keyword.trim() : "";
        page = page != null && page > 0 ? page : 1;
        size = size != null && size > 0 ? size : 10;
        orderBy = orderBy != null ? orderBy : "createdAt";
        sortBy = "asc".equalsIgnoreCase(sortBy) ? "asc" : "desc";
        nickname = nickname != null ? nickname.trim() : "";
    }
}
