package com.spring.multiboardbackend.domain.member;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MemberVo {

    private Long memberId;                  // pk
    private String loginId;                 // 로그인 아이디
    private String password;                // 비밀번호
    private String nickname;                // 이름
    private int deleteYn;                   // 삭제 1 미삭제 0
    private LocalDateTime createdDate;      // 생성일시
    private LocalDateTime updatedDate;      // 수정일시
}
