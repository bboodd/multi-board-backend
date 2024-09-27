package com.hh.multiboarduserbackend.domain.member.response;

import com.hh.multiboarduserbackend.jwt.JwtToken;
import com.hh.multiboarduserbackend.mappers.MemberMapper;
import lombok.Builder;

@Builder
public record LogInResponseDto(
          JwtToken token
        , String nickname
) {
}
