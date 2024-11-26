package com.spring.multiboardbackend.domain.member.dto.response;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String loginId,
        String nickname,
        LocalDateTime lastLoginAt
) {
}
