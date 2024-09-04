package com.hh.multiboarduserbackend.domain.member;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberVo(
        Long memberId
        , String loginId
        , String password
        , String nickname
        , int deleteYn
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
) {
}
