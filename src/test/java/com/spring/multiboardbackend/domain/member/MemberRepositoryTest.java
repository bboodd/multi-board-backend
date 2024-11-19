package com.spring.multiboardbackend.domain.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 멤버찾기_로그인아이디_성공() {
        //given
        MemberVo member = memberRepository.findByLoginId("qw12");

        //when

        //then
        assertThat(member.getNickname()).isEqualTo("qw12");
        assertThat(member.getLoginId()).isEqualTo("qw12");
    }

    @Test
    void 멤버찾기_로그인아이디_실패() {
        //given
        Optional<MemberVo> member = Optional.ofNullable(memberRepository.findByLoginId("잘못된아이디"));

        //when

        //then
        assertTrue(member.isEmpty());
    }

    @Test
    void 회원가입_성공() {
        //given
        MemberVo member = MemberVo.builder().loginId("아이디").password("비밀번호").nickname("닉네임").build();

        //when
        memberRepository.save(member);
        Optional<MemberVo> findMember = Optional.ofNullable(memberRepository.findByLoginId("아이디"));

        //then
        assertThat(findMember.get().getNickname()).isEqualTo(member.getNickname());
    }
}