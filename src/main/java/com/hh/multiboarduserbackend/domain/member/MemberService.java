package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.exception.CustomException;
import com.hh.multiboarduserbackend.exception.MemberErrorCode;
import com.hh.multiboarduserbackend.jwt.JwtProvider;
import com.hh.multiboarduserbackend.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private Optional<MemberVo> findByLoginId(String loginId) {
        return Optional.ofNullable(memberRepository.findByLoginId(loginId));
    }

    private Optional<MemberVo> findById(Long memberId) {
        return Optional.ofNullable(memberRepository.findById(memberId));
    }

    /**
     * 회원가입 로직
     * @param memberVo - 회원가입 정보
     * @return - 회원 ID
     */
    public Long signUp(MemberVo memberVo) {
        duplicateCheck(memberVo.getLoginId());
        memberRepository.save(memberVo);
        return memberVo.getMemberId();
    }

    /**
     * 로그인 로직
     * @param memberVo - 로그인 정보
     * @return - jwt token
     */
    public JwtToken logIn(MemberVo memberVo) {
        // 아이디에 해당하는 회원이 없을 시 에러
        MemberVo findMember = findByLoginId(memberVo.getLoginId())
                .orElseThrow(() -> MemberErrorCode.LOGIN_UNAUTHORIZED.defaultException());

        // 저장된 비밀번호가 일치하지 않을 시 에러
        if(!findMember.getPassword().equals(memberVo.getPassword())) {
            throw MemberErrorCode.LOGIN_UNAUTHORIZED.defaultException();
        }

        JwtToken jwtToken = jwtProvider.generateToken(findMember.getMemberId());
        return jwtToken;
    }

    /**
     * 로그인 아이디 중복확인 검증 로직
     * @param loginId - 로그인 아이디
     */
    public void duplicateCheck(String loginId) {
        boolean duplicate = findByLoginId(loginId).isPresent();
        if(duplicate) {
            throw MemberErrorCode.DUPLICATE_LOGIN_ID.defaultException();
        }
    }


}
