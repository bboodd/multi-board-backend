package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.mapper.MemberMapper;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
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
    private final MemberMapper memberMapper;

    /**
     * ID로 회원 조회
     *
     * @param id 조회할 회원 ID
     * @return 회원 정보 Response
     */
    public MemberResponse findById(Long id) {
        return memberRepository.findById(id)
                .map(memberMapper::toResponse)
                .orElseThrow(MemberErrorCode.MEMBER_NOT_FOUND::defaultException);
    }

}