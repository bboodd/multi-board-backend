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

import java.util.Optional;

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
    private MemberMapper memberMapper;

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
            SignUpRequest request = createSignUpRequest();
            MemberVO memberVO = createMemberVO();
            MemberResponse expectedResponse = createMemberResponse();

            given(memberRepository.existsByLoginId(request.loginId())).willReturn(false);
            given(memberRepository.existsByNickname(request.nickname())).willReturn(false);
            given(memberMapper.toVO(request)).willReturn(memberVO);
            given(passwordEncoder.encode(request.password())).willReturn("encodedPassword");
            given(memberMapper.toResponse(any(MemberVO.class))).willReturn(expectedResponse);

            // when
            MemberResponse result = authService.signUp(request);

            // then
            assertThat(result.id()).isEqualTo(expectedResponse.id());
            assertThat(result.loginId()).isEqualTo(expectedResponse.loginId());
            verify(memberRepository).save(any(MemberVO.class));
        }

        @Test
        @DisplayName("중복된 로그인 아이디로 가입 시도시 실패")
        void signUp_DuplicateLoginId_Fail() {
            // given
            SignUpRequest request = createSignUpRequest();
            given(memberRepository.existsByLoginId(request.loginId())).willReturn(true);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.signUp(request));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.DUPLICATE_LOGIN_ID);
        }

        @Test
        @DisplayName("중복된 닉네임으로 가입 시도시 실패")
        void signUp_DuplicateNickname_Fail() {
            // given
            SignUpRequest request = createSignUpRequest();
            given(memberRepository.existsByLoginId(request.loginId())).willReturn(false);
            given(memberRepository.existsByNickname(request.nickname())).willReturn(true);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.signUp(request));

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
            LoginRequest request = createLoginRequest();
            MemberVO member = createMemberVO();
            JwtToken token = createJwtToken();
            LoginResponse expectedResponse = createLoginResponse();

            given(memberRepository.findByLoginId(request.loginId())).willReturn(Optional.of(member));
            given(passwordEncoder.matches(request.password(), member.getPassword())).willReturn(true);
            given(jwtProvider.generateToken(member.getId())).willReturn(token);
            given(memberMapper.toLoginResponse(token, member.getNickname())).willReturn(expectedResponse);

            // when
            LoginResponse result = authService.login(request);

            // then
            assertThat(result.token()).isEqualTo(expectedResponse.token());
            assertThat(result.nickname()).isEqualTo(expectedResponse.nickname());
            verify(memberRepository).updateLastLoginAt(member.getId());
        }

        @Test
        @DisplayName("존재하지 않는 아이디로 로그인 시도시 실패")
        void login_NonexistentLoginId_Fail() {
            // given
            LoginRequest request = createLoginRequest();
            given(memberRepository.findByLoginId(request.loginId())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.login(request));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 시도시 실패")
        void login_InvalidPassword_Fail() {
            // given
            LoginRequest request = createLoginRequest();
            MemberVO member = createMemberVO();

            given(memberRepository.findByLoginId(request.loginId())).willReturn(Optional.of(member));
            given(passwordEncoder.matches(request.password(), member.getPassword())).willReturn(false);

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> authService.login(request));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.INVALID_PASSWORD);
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

    private SignUpRequest createSignUpRequest() {
        return SignUpRequest.of("testUser", "Password123!", "Password123!", "테스트유저");
    }

    private LoginRequest createLoginRequest() {
        return new LoginRequest("testUser", "Password123!");
    }

    private MemberVO createMemberVO() {
        return MemberVO.builder()
                .id(1L)
                .loginId("testUser")
                .password("encodedPassword")
                .nickname("테스트유저")
                .build();
    }

    private MemberResponse createMemberResponse() {
        return new MemberResponse(1L, "testUser", "테스트유저");
    }

    private LoginResponse createLoginResponse() {
        return new LoginResponse(createJwtToken(), "테스트유저");
    }

    private JwtToken createJwtToken() {
        return new JwtToken("Bearer", "accessToken", "refreshToken");
    }
}