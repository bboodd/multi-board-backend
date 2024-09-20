package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.member.LogInRequestDto;
import com.hh.multiboarduserbackend.domain.member.LogInResponseDto;
import com.hh.multiboarduserbackend.domain.member.MemberVo;
import com.hh.multiboarduserbackend.domain.member.SignUpDto;
import com.hh.multiboarduserbackend.jwt.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberVo toVo(SignUpDto signUpDto);

    MemberVo toVo(LogInRequestDto loginRequestDto);

    LogInResponseDto toDto(JwtToken token, String nickname);
}
