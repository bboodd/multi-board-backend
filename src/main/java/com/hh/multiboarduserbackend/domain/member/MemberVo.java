package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.mappers.MemberMapper;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberVo(
          Long memberId
        , String loginId
        , String password
        , String nickname
        , int deleteYn
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
) {

    public static MemberVo toVo(SignUpDto signUpDto) {
        return MemberMapper.INSTANCE.toMemberVo(signUpDto);
    }

    public static MemberVo toVo(LogInDto logInDto) {
        return MemberMapper.INSTANCE.toMemberVo(logInDto);
    }
}
