package com.hh.multiboarduserbackend.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.multiboarduserbackend.exception.CustomException;
import com.hh.multiboarduserbackend.exception.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api-board/members";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void 회원가입검증_성공() throws Exception {
        //given
        SignUpDto signUpDto = SignUpDto.builder()
                .loginId("zxcv")
                .password("qw12!")
                .checkPassword("qw12!")
                .nickname("zxcv")
                .build();
        String json = objectMapper.writeValueAsString(signUpDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api-board/members/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("회원가입 완료"));
    }

    @Test
    void 회원가입검증_연속된비밀번호_실패() throws Exception {
        //given
        SignUpDto signUpDto = SignUpDto.builder()
                .loginId("zxcv")
                .password("qqq111!")
                .checkPassword("qqq111!")
                .nickname("zxcv")
                .build();
        String json = objectMapper.writeValueAsString(signUpDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api-board/members/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호에 동일만 문자 혹은 숫자는 3회 이상 반복할 수 없습니다."));
    }

    @Test
    void 아이디_중복체크_중복() throws Exception {
        //given
        DuplicateCheckRequestDto check = DuplicateCheckRequestDto.builder().loginId("qw12").build();
        String json = objectMapper.writeValueAsString(check);
        String loginId = "{\"loginId\":\"qw12\"}";


        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api-board/members/check-duplicate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

}