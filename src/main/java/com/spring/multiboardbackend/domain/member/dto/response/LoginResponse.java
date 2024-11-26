package com.spring.multiboardbackend.domain.member.dto.response;

import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "사용자 닉네임", example = "사용자123")
        JwtToken token,
        @Schema(description = "사용자 닉네임", example = "사용자123")
        String nickname
) {
}
