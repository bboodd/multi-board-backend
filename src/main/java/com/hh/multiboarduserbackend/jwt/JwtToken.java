package com.hh.multiboarduserbackend.jwt;

import lombok.Builder;
import lombok.Data;

@Builder
public record JwtToken(
          String grantType
        , String accessToken
        , String refreshToken
) {
}
