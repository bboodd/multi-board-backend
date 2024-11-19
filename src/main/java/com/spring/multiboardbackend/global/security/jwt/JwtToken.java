package com.spring.multiboardbackend.global.security.jwt;

import lombok.Builder;

@Builder
public record JwtToken(
          String grantType
        , String accessToken
        , String refreshToken
) {
}
