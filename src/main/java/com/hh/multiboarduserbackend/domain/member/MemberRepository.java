package com.hh.multiboarduserbackend.domain.member;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("member")
public interface MemberRepository {

    void saveMember(MemberVo memberVo);

    Optional<MemberVo> findById(Long userId);

    int countAllByLoginId(String loginId);
}