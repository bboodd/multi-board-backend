package com.spring.multiboardbackend.domain.post.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentVO {

    private Long id;                 // pk
    private Long postId;                    // fk
    private Long memberId;                  // fk
    private String content;                 // 내용
    private LocalDateTime createdAt;      // 만들 날짜
    private boolean deleted;                   // 삭제시 1 미삭제 0
    private String nickname;                // 서브쿼리문
}
