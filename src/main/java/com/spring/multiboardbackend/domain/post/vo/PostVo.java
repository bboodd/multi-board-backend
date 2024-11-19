package com.spring.multiboardbackend.domain.post.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {

    private Long postId;                    // pk
    private Long typeId;                    // fk
    private Long categoryId;                // fk
    private String title;                   // 제목
    private String content;                 // 내용
    private int viewCnt;                    // 조회수
    private LocalDateTime createdDate;      // 생성일시
    private LocalDateTime updatedDate;      // 수정일시
    private boolean deleteYn;               // 삭제 여부
    private boolean lockYn;                 // 비밀글 여부
    private boolean finYn;                  // 고정글 여부
    private Long adminId;                   // 관리자 pk
    private Long memberId;                  // 멤버 pk

    private String categoryName;            // 서브쿼리문
    private String nickname;                // 서브쿼리문
    private int fileCnt;                    // 서브쿼리문
    private int commentCnt;                 // 서브쿼리문
    private String thumbnailUrl;            // 서브쿼리문
}
