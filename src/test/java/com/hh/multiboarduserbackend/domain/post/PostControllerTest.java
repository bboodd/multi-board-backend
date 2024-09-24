package com.hh.multiboarduserbackend.domain.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.multiboarduserbackend.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void 게시글_저장_성공() throws Exception{
        //given
        String accessToken = jwtProvider.generateToken(1L).accessToken();

        //when
        ResultActions resultActions = mockMvc.perform(multipart("/api-board/free/posts")
                .header("Authorization", "Bearer "+accessToken)
                .param("content", "내용")
                .param("categoryId", "2")
                .param("title", "제목")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions
                .andExpect(status().isCreated());

    }
}