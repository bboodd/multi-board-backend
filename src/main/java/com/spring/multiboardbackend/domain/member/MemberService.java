package com.spring.multiboardbackend.domain.member;

import com.spring.multiboardbackend.domain.member.response.LogInResponseDto;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
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

    private Optional<MemberVo> findByNickname(String nickname) {
        return Optional.ofNullable(memberRepository.findByNickname(nickname));
    }

    /**
     * 회원가입 로직
     * @param memberVo - 회원가입 정보
     * @return - 회원 ID
     */
    public Long signUp(MemberVo memberVo) {
        duplicateCheckLoginId(memberVo.getLoginId());
        duplicateCheckNickname(memberVo.getNickname());
        // 비밀번호 암호화
        memberVo.setPassword(encodePassword(memberVo.getPassword()));

        memberRepository.save(memberVo);
        return memberVo.getMemberId();
    }

    /**
     * 로그인 로직
     * @param memberVo - 로그인 정보
     * @return - jwt token + nickname
     */
    public LogInResponseDto logIn(MemberVo memberVo) {
        // 아이디에 해당하는 회원이 없을 시 에러
        MemberVo findMember = findByLoginId(memberVo.getLoginId())
                .orElseThrow(MemberErrorCode.NOT_MATCH_ERROR::defaultException);

        // 저장된 비밀번호가 일치하지 않을 시 에러
        if(!comparePassword(memberVo.getPassword(), findMember.getPassword())) {
            throw MemberErrorCode.NOT_MATCH_ERROR.defaultException();
        }

        JwtToken jwtToken = jwtProvider.generateToken(findMember.getMemberId());

        LogInResponseDto tokenAndNickname = LogInResponseDto.builder()
                .token(jwtToken)
                .nickname(findMember.getNickname())
                .build();
        return tokenAndNickname;
    }

    /**
     * 로그인 아이디 중복확인 검증 로직
     * @param loginId - 로그인 아이디
     */
    public void duplicateCheckLoginId(String loginId) {
        if(findByLoginId(loginId).isPresent()) {
            throw MemberErrorCode.DUPLICATE_LOGIN_ID.defaultException();
        }
    }

    /**
     * 닉네임 중복확인 로직
     * @param nickname - 입력 닉네임
     */
    public void duplicateCheckNickname(String nickname) {
        if(findByNickname(nickname).isPresent()) {
            throw MemberErrorCode.DUPLICATE_NICKNAME.defaultException();
        }
    }

    /**
     * Bcrypt 비밀번호 암호화
     * @param password - 비밀번호 평문
     * @return - 암호화 비밀번호
     */
    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean comparePassword(String inputPas, String savedPas) {
        return BCrypt.checkpw(inputPas, savedPas);
    }


}
