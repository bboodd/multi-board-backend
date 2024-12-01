package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 서비스 테스트")
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입")
    class SignUp {

        @Test
        @DisplayName("회원가입 성공")
        void signUp_Success() {
            // given
            MemberVO member = createMemberVO();

            given(memberRepository.existsByLoginId(member.getLoginId())).willReturn(false);
            given(memberRepository.existsByNickname(member.getNickname())).willReturn(false);
            given(passwordEncoder.encode(member.getPassword())).willReturn("encodedPassword");

            // when
            MemberVO result = authService.signUp(member);

            // then
            assertThat(result.getId()).isNull();
            assertThat(member.getId()).isEqualTo(1L);
            assertThat(result.getLoginId()).isEqualTo(member.getLoginId());
            verify(memberRepository).save(any(MemberVO.class));
        }

        @Test
        @DisplayName("중복된 로그인 아이디로 가입 시도시 실패")
        void signUp_DuplicateLoginId_Fail() {
            // given
            MemberVO member = createMemberVO();
            given(memberRepository.existsByLoginId(member.getLoginId())).willReturn(true);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.signUp(member));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.DUPLICATE_LOGIN_ID);
        }

        @Test
        @DisplayName("중복된 닉네임으로 가입 시도시 실패")
        void signUp_DuplicateNickname_Fail() {
            // given
            MemberVO member = createMemberVO();
            given(memberRepository.existsByLoginId(member.getLoginId())).willReturn(false);
            given(memberRepository.existsByNickname(member.getNickname())).willReturn(true);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.signUp(member));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @DisplayName("로그인 성공")
        void login_Success() {
            // given
            Long memberId = 1L;
            JwtToken expectedToken = createJwtToken();

            given(jwtProvider.generateToken(memberId)).willReturn(expectedToken);

            // when
            JwtToken result = authService.login(memberId);

            // then
            assertThat(result).isEqualTo(expectedToken);
            verify(memberRepository).updateLastLoginAt(memberId);
        }
    }

    @Nested
    @DisplayName("권한 확인")
    class CheckRole {

        @Test
        @DisplayName("권한 확인 성공")
        void hasRole_Success() {
            // given
            Long memberId = 1L;
            Role role = Role.ADMIN;
            given(memberRepository.hasRole(memberId, role.getKey())).willReturn(true);

            // when
            boolean result = authService.hasRole(memberId, role);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("관리자 권한 확인")
        void isAdmin_Success() {
            // given
            Long memberId = 1L;
            given(memberRepository.hasRole(memberId, Role.ADMIN.getKey())).willReturn(true);

            // when
            boolean result = authService.isAdmin(memberId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("중복 검사")
    class CheckDuplicate {

        @Test
        @DisplayName("로그인 아이디 중복 검사")
        void checkDuplicate_LoginId_Success() {
            // given
            String loginId = "testUser";
            given(memberRepository.existsByLoginId(loginId)).willReturn(false);

            // when
            boolean result = authService.checkDuplicate(loginId, "LOGIN_ID");

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("닉네임 중복 검사")
        void checkDuplicate_Nickname_Success() {
            // given
            String nickname = "테스트유저";
            given(memberRepository.existsByNickname(nickname)).willReturn(false);

            // when
            boolean result = authService.checkDuplicate(nickname, "NICKNAME");

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("잘못된 검사 타입으로 요청시 실패")
        void checkDuplicate_InvalidType_Fail() {
            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> authService.checkDuplicate("test", "INVALID_TYPE"));
        }
    }

    private MemberVO createMemberVO() {
        return MemberVO.builder()
                .id(1L)
                .loginId("testUser")
                .password("Password123!")
                .nickname("테스트유저")
                .build();
    }

    private JwtToken createJwtToken() {
        return new JwtToken("Bearer", "accessToken", "refreshToken");
    }
}