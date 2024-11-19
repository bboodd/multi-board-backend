package com.spring.multiboardbackend.domain.member;

import com.spring.multiboardbackend.domain.member.request.LogInRequestDto;
import com.spring.multiboardbackend.domain.member.response.LogInResponseDto;
import com.spring.multiboardbackend.domain.member.request.SignUpDto;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberVo toVo(SignUpDto signUpDto);

    MemberVo toVo(LogInRequestDto loginRequestDto);

    LogInResponseDto toDto(JwtToken token, String nickname);
}
