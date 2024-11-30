package com.spring.multiboardbackend.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.domain.member.dto.request.DuplicateCheckRequest;
import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.LoginResponse;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 컨트롤러 테스트")
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @Nested
    @DisplayName("회원가입")
    class SignUp {

        @Test
        @DisplayName("회원가입 성공")
        void signUp_Success() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.of(
                    "testUser",
                    "Password123!",
                    "Password123!",
                    "테스트유저"
            );
            MemberResponse expectedResponse = new MemberResponse(1L, "testUser", "테스트유저");
            given(authService.signUp(any(SignUpRequest.class))).willReturn(expectedResponse);

            // when & then
            mockMvc.perform(post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.loginId").value("testUser"))
                    .andExpect(jsonPath("$.nickname").value("테스트유저"));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @DisplayName("로그인 성공")
        void login_Success() throws Exception {
            // given
            LoginRequest request = new LoginRequest("testUser", "Password123!");
            LoginResponse expectedResponse = new LoginResponse(
                    new JwtToken("Bearer", "test-access-token", "test-refresh-token"),
                    "테스트유저"
            );
            given(authService.login(any(LoginRequest.class))).willReturn(expectedResponse);

            // when & then
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token.accessToken").value("test-access-token"))
                    .andExpect(jsonPath("$.nickname").value("테스트유저"));
        }
    }

    @Nested
    @DisplayName("중복 검사")
    class CheckDuplicate {

        @Test
        @DisplayName("로그인 ID 중복 검사")
        void checkDuplicateLoginId_Success() throws Exception {
            // given
            DuplicateCheckRequest request = new DuplicateCheckRequest("testUser");
            given(authService.checkDuplicate("testUser", "LOGIN_ID")).willReturn(true);

            // when & then
            mockMvc.perform(post("/api/auth/check-duplicate/login-id")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true));
        }

        @Test
        @DisplayName("닉네임 중복 검사")
        void checkDuplicateNickname_Success() throws Exception {
            // given
            DuplicateCheckRequest request = new DuplicateCheckRequest("테스트유저");
            given(authService.checkDuplicate("테스트유저", "NICKNAME")).willReturn(true);

            // when & then
            mockMvc.perform(post("/api/auth/check-duplicate/nickname")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true));
        }
    }
}