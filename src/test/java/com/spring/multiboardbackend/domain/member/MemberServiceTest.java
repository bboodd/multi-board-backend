package com.spring.multiboardbackend.domain.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.service.MemberService;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtProvider jwtProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 회원가입테스트_성공() {
        //given
        MemberVO memberVo = MemberVO.builder()
                .loginId("qwer").password("1234").nickname("abcd").build();

        //when
        memberService.signUp(memberVo);

        //then
        then(memberRepository).should().save(any());
    }

    @Test
    void 회원가입검증테스트_중복_실패() {
        //given
        MemberVO memberVo = MemberVO.builder()
                .loginId("qwer").password("1234").nickname("abcd").build();
        given(memberRepository.findByLoginId("qwer")).willReturn(memberVo);
        //when

        //then
        assertThrows(CustomException.class, () -> {
            memberService.signUp(memberVo);
        });
    }

    @Test
    void 로그인테스트_성공() {
        //given
        MemberVO memberVo = MemberVO.builder().memberId(1L)
                .loginId("qwer").password("1234").nickname("abcd").build();
        JwtToken token = JwtToken.builder()
                .accessToken("토큰").build();
        given(memberRepository.findByLoginId("qwer")).willReturn(memberVo);
        given(jwtProvider.generateToken(1L)).willReturn(token);

        //when
        JwtToken logInToken = memberService.logIn(memberVo).token();

        //then
        assertThat(logInToken.accessToken()).isEqualTo(token.accessToken());
    }

    @Test
    void 로그인검증테스트_없는아이디_실패() {
        //given
        MemberVO memberVo = MemberVO.builder().memberId(1L)
                .loginId("qwer").password("1234").nickname("abcd").build();

        given(memberRepository.findByLoginId("qwer")).willReturn(null);

        //when

        //then
        assertThrows(CustomException.class, () -> {
            memberService.logIn(memberVo);
        });
    }

    @Test
    void 로그인검증테스트_틀린비밀번호_실패() {
        //given
        MemberVO memberVo = MemberVO.builder().memberId(1L)
                .loginId("qwer").password("1234").nickname("abcd").build();
        MemberVO memberVO2 = MemberVO.builder().memberId(2L)
                .loginId("qwer").password("2345").nickname("abcd").build();
        given(memberRepository.findByLoginId("qwer")).willReturn(memberVO2);
        //when

        //then
        assertThrows(CustomException.class, () -> {
            memberService.logIn(memberVo);
        });
    }

}