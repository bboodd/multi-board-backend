package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.member.request.LogInRequestDto;
import com.hh.multiboarduserbackend.domain.member.response.LogInResponseDto;
import com.hh.multiboarduserbackend.domain.member.MemberVo;
import com.hh.multiboarduserbackend.domain.member.request.SignUpDto;
import com.hh.multiboarduserbackend.jwt.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberVo toVo(SignUpDto signUpDto);

    MemberVo toVo(LogInRequestDto loginRequestDto);

    LogInResponseDto toDto(JwtToken token, String nickname);
}
