package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.LoginResponse;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.mapper.MemberMapper;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     *
     * @param request 회원가입 요청 정보
     * @return 가입된 회원 정보 Response
     */
    @Transactional
    public MemberResponse signUp(SignUpRequest request) {
        validateDuplicateLoginId(request.loginId());
        validateDuplicateNickname(request.nickname());

        MemberVO member = memberMapper.toVO(request);

        member = MemberVO.builder()
                .loginId(member.getLoginId())
                .password(passwordEncoder.encode(request.password()))
                .nickname(member.getNickname())
                .build();

        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }

    /**
     * 로그인 처리
     *
     * @param request 로그인 요청 정보
     * @return 로그인 결과 Response
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        MemberVO member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(MemberErrorCode.MEMBER_NOT_FOUND::defaultException);

        validatePassword(request.password(), member.getPassword());

        memberRepository.updateLastLoginAt(member.getId());

        JwtToken token = jwtProvider.generateToken(member.getId());
        return memberMapper.toLoginResponse(token, member.getNickname());
    }

    /**
     * 회원의 특정 권한 보유 여부 확인
     *
     * @param memberId 회원 ID
     * @param role 확인할 권한
     * @return 권한 보유 여부
     */
    public boolean hasRole(Long memberId, Role role) {
        return memberRepository.hasRole(memberId, role.getKey());
    }

    /**
     * 회원의 관리자 권한 보유 여부 확인
     *
     * @param memberId 회원 ID
     * @return 관리자 여부
     */
    public boolean isAdmin(Long memberId) {
        return hasRole(memberId, Role.ADMIN);
    }

    /**
     * 아이디/닉네임 중복 검사
     *
     * @param value 검사할 값
     * @param type 검사 유형(LOGIN_ID/NICKNAME)
     * @return 사용 가능 여부
     */
    public boolean checkDuplicate(String value, String type) {
        boolean isDuplicate = switch (type.toUpperCase()) {
            case "LOGIN_ID" -> memberRepository.existsByLoginId(value);
            case "NICKNAME" -> memberRepository.existsByNickname(value);
            default -> throw new IllegalArgumentException("Invalid check type: " + type);
        };

        return !isDuplicate;
    }

    /**
     * 로그인 아이디 중복 확인
     *
     * @param loginId 확인할 로그인 아이디
     */
    private void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw MemberErrorCode.DUPLICATE_LOGIN_ID.defaultException();
        }
    }

    /**
     * 닉네임 중복 확인
     *
     * @param nickname 확인할 닉네임
     */
    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw MemberErrorCode.DUPLICATE_NICKNAME.defaultException();
        }
    }

    /**
     * 비밀번호 일치 여부 검증
     *
     * @param rawPassword 입력된 비밀번호
     * @param encodedPassword 저장된 암호화 비밀번호
     */
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw MemberErrorCode.INVALID_PASSWORD.defaultException();
        }
    }

}