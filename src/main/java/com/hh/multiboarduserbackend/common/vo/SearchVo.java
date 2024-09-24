package com.hh.multiboarduserbackend.common.vo;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVo {

    private LocalDateTime startDate;    // 검색 시작날짜
    private LocalDateTime endDate;      // 검색 종료날짜
    private Long categoryId;            // 검색 카테고리 아이디
    private String keyword;             // 검색 키워드
    private int page;                   // 현재 페이지 번호
    private int recordSize;             // 페이지당 출력할 데이터 개수
    private int pageSize;               // 화면 하단 출력할 페이지 사이즈
    private int limitStart;             // 쿼리 limit 변수
    private String orderBy;             // 정렬 조건
    private String sortBy;              // 정렬 방법
    private String nickname;            // 나의 문의 내역만 보기
    private Long typeId;                // 게시판 타입 pk
}
