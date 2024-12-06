package com.spring.multiboardbackend.domain.post.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostVO {
    // 기본 정보
    private Long id;
    private Long boardTypeId;
    private Long categoryId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 상태 플래그
    private boolean deleted;
    private boolean locked;
    private boolean fixed;

    // 연관 ID
    private Long memberId;

    // 추가 정보 (조인/서브쿼리)
    private String categoryName;
    private String nickname;
    private int fileCount;
    private int commentCount;
    private List<FileVO> files;
    private List<CommentVO> comments;
}
