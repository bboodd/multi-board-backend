package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.jwt.JwtToken;
import com.hh.multiboarduserbackend.mappers.MemberMapper;
import lombok.Builder;

@Builder
public record LogInResponseDto(
          JwtToken token
        , String nickname
) {

    public static LogInResponseDto toDto(JwtToken token, String nickname) {
        return MemberMapper.INSTANCE.toDto(token, nickname);
    }
}
