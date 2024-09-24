package com.hh.multiboarduserbackend.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private Long commentId;                 // pk
    private Long postId;                    // fk
    private Long memberId;                  // fk
    private String content;                 // 내용
    private LocalDateTime createdDate;      // 만들 날짜
    private int deleteYn;                   // 삭제시 1 미삭제 0
    private String nickname;                // 서브쿼리문
}
