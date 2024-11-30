package com.spring.multiboardbackend.domain.member.dto.response;


public record MemberResponse(
        Long id,
        String loginId,
        String nickname
) {
}
