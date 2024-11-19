package com.spring.multiboardbackend.domain.member.response;

import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import lombok.Builder;

@Builder
public record LogInResponseDto(
          JwtToken token
        , String nickname
) {
}
