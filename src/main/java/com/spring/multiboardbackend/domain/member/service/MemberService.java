package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * ID로 회원 조회
     *
     * @param id 조회할 회원 ID
     * @return 회원 정보 Response
     */
    public MemberVO findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberErrorCode.MEMBER_NOT_FOUND::defaultException);
    }

    /**
     * 로그인 ID로 회원 조회
     *
     * @param loginId 조회할 로그인 ID
     * @return 회원 정보 Response
     */
    public MemberVO findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
               .orElseThrow(MemberErrorCode.MEMBER_NOT_FOUND::defaultException);
    }
}