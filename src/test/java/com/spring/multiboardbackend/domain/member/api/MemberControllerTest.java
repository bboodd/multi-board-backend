package com.spring.multiboardbackend.domain.member.api;

import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.domain.member.service.MemberService;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 Controller 테스트")
class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(memberController)
                .build();

        AuthenticationContextHolder.clear();
    }

    @Nested
    @DisplayName("회원 정보 조회")
    class GetMemberInfo {

        @Test
        @DisplayName("내 정보 조회 성공")
        void getMyInfo_Success() throws Exception {
            // given
            Long memberId = 1L;
            MemberResponse expectedResponse = createMemberResponse(memberId);

            AuthenticationContextHolder.setContext(memberId);
            given(memberService.findById(memberId)).willReturn(expectedResponse);

            // when & then
            mockMvc.perform(get("/api/members/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(memberId))
                    .andExpect(jsonPath("$.loginId").value("testUser"))
                    .andExpect(jsonPath("$.nickname").value("테스트유저"));
        }
    }

    private MemberResponse createMemberResponse(Long id) {
        return new MemberResponse(id, "testUser", "테스트유저");
    }
}