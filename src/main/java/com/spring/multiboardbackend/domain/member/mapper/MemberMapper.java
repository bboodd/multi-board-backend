package com.spring.multiboardbackend.domain.member.mapper;

import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.LoginResponse;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",  // 스프링 빈으로 등록
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MemberMapper {

    @Mapping(target = "password", ignore = true)
    MemberVO toVO(SignUpRequest request);

    LoginResponse toLoginResponse(JwtToken token, String nickname);

    MemberResponse toResponse(MemberVO memberVo);
}
