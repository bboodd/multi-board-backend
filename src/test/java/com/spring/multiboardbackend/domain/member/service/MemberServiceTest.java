package com.spring.multiboardbackend.domain.member.service;

import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.repository.MemberRepository;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 서비스 테스트")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 조회")
    class FindById {

        @Test
        @DisplayName("ID로 회원 조회 성공")
        void findById_Success() {
            // given
            Long memberId = 1L;
            MemberVO member = createMemberVO(memberId);

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            // when
            MemberVO result = memberService.findById(memberId);

            // then
            assertThat(result.getId()).isEqualTo(memberId);
            assertThat(result.getLoginId()).isEqualTo("testUser");
            assertThat(result.getNickname()).isEqualTo("테스트유저");
            verify(memberRepository).findById(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 회원 ID로 조회시 실패")
        void findById_NotFound_Fail() {
            // given
            Long nonExistentMemberId = 999L;
            given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> memberService.findById(nonExistentMemberId));

            assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
            verify(memberRepository).findById(nonExistentMemberId);
        }
    }

    private MemberVO createMemberVO(Long id) {
        return MemberVO.builder()
                .id(id)
                .loginId("testUser")
                .password("hashedPassword")
                .nickname("테스트유저")
                .build();
    }
}