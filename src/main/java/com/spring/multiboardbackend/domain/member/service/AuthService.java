package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param member 등록할 회원 정보
     * @return 등록된 회원 정보
     */
    @Transactional
    public MemberVO signUp(MemberVO member) {
        validateDuplicateLoginId(member.getLoginId());
        validateDuplicateNickname(member.getNickname());

        MemberVO signup = MemberVO.builder()
                .loginId(member.getLoginId())
                .password(passwordEncoder.encode(member.getPassword()))
                .nickname(member.getNickname())
                .build();

        memberRepository.save(signup);

        return signup;
    }

    /**
     * 회원 로그인을 처리하고 JWT 토큰을 발급합니다.
     *
     * @param loginId 로그인할 아이디
     * @param password 로그인할 패스워드
     * @return memberVO
     */
    @Transactional
    public MemberVO login(String loginId, String password) {

        MemberVO member = memberRepository.findByLoginId(loginId)
                        .orElseThrow(MemberErrorCode.MEMBER_NOT_FOUND::defaultException);

        validateLoginPassword(password, member.getPassword());

        memberRepository.updateLastLoginAt(member.getId());

        return member;
    }

    /**
     * 회원이 특정 권한을 가지고 있는지 확인합니다.
     *
     * @param memberId 확인할 회원의 ID
     * @param role 확인할 권한
     * @return 권한 보유 여부
     */
    public boolean hasRole(Long memberId, Role role) {
        return memberRepository.hasRole(memberId, role.getKey());
    }

    /**
     * 회원이 관리자 권한을 가지고 있는지 확인합니다.
     *
     * @param memberId 확인할 회원의 ID
     * @return 관리자 권한 보유 여부
     */
    public boolean isAdmin(Long memberId) {
        return hasRole(memberId, Role.ADMIN);
    }

    /**
     * 아이디 또는 닉네임의 중복 여부를 확인합니다.
     *
     * @param value 검사할 값
     * @param type 검사 유형 (LOGIN_ID/NICKNAME)
     * @return 사용 가능 여부 (true: 사용 가능, false: 중복)
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
     * 로그인 아이디의 중복 여부를 확인합니다.
     *
     * @param loginId 확인할 로그인 아이디
     */
    private void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw MemberErrorCode.DUPLICATE_LOGIN_ID.defaultException();
        }
    }

    /**
     * 닉네임의 중복 여부를 확인합니다.
     *
     * @param nickname 확인할 닉네임
     */
    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw MemberErrorCode.DUPLICATE_NICKNAME.defaultException();
        }
    }


    private void validateLoginPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw MemberErrorCode.INVALID_PASSWORD.defaultException();
        }
    }
}