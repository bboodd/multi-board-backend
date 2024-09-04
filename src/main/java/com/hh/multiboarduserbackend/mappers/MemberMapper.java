package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.domain.member.MemberVo;
import com.hh.multiboarduserbackend.domain.member.SignInDto;
import com.hh.multiboarduserbackend.domain.member.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberVo toMemberVo(SignUpDto signUpDto);

    MemberVo toMemberVo(SignInDto signInDto);
}
