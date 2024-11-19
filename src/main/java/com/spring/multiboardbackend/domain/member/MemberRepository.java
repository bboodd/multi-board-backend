package com.spring.multiboardbackend.domain.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRepository {

    void save(MemberVo memberVo);

    MemberVo findById(Long userId);

    MemberVo findByLoginId(String loginId);

    MemberVo findByNickname(String nickname);
}
