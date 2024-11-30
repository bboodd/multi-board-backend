package com.spring.multiboardbackend.domain.member.repository;

import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("회원 Repository 테스트")
@Sql("/sql/member-test-data.sql")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 저장")
    class Save {

        @Test
        @DisplayName("회원 저장 성공")
        void save_Success() {
            // given
            MemberVO member = createMemberVO("newUser", 1L);

            // when
            memberRepository.save(member);

            // then
            Optional<MemberVO> savedMember = memberRepository.findByLoginId("newUser");
            assertThat(savedMember)
                    .isPresent()
                    .hasValueSatisfying(m -> assertThat(m)
                            .extracting(
                                    MemberVO::getLoginId,
                                    MemberVO::getNickname,
                                    MemberVO::getRoleId
                            )
                            .containsExactly(
                                    "newUser",
                                    "신규사용자",
                                    1L
                            ));
        }
    }

    @Nested
    @DisplayName("회원 조회")
    class Find {

        @Test
        @DisplayName("ID로 회원 조회 성공")
        void findById_Success() {
            // given
            Long memberId = 1L;

            // when
            Optional<MemberVO> member = memberRepository.findById(memberId);

            // then
            assertThat(member)
                    .isPresent()
                    .hasValueSatisfying(m -> assertThat(m)
                            .extracting(
                                    MemberVO::getId,
                                    MemberVO::getRoleId,
                                    MemberVO::isDeleted
                            )
                            .containsExactly(
                                    memberId,
                                    1L,
                                    false
                            ));
        }

        @Test
        @DisplayName("로그인 ID로 회원 조회 성공")
        void findByLoginId_Success() {
            // given
            String loginId = "testUser";

            // when
            Optional<MemberVO> member = memberRepository.findByLoginId(loginId);

            // then
            assertThat(member)
                    .isPresent()
                    .hasValueSatisfying(m -> assertThat(m)
                            .extracting(
                                    MemberVO::getLoginId,
                                    MemberVO::getRoleId
                            )
                            .containsExactly(
                                    loginId,
                                    1L
                            ));
        }
    }

    @Nested
    @DisplayName("중복 검사")
    class Exists {

        @Test
        @DisplayName("로그인 ID와 닉네임 중복 확인")
        void exists_Success() {
            // given
            String existingLoginId = "testUser";
            String nonExistingLoginId = "newUser";
            String existingNickname = "테스트유저";
            String nonExistingNickname = "신규유저";

            // then
            assertThat(memberRepository)
                    .satisfies(repo -> {
                        assertThat(repo.existsByLoginId(existingLoginId)).isTrue();
                        assertThat(repo.existsByLoginId(nonExistingLoginId)).isFalse();
                        assertThat(repo.existsByNickname(existingNickname)).isTrue();
                        assertThat(repo.existsByNickname(nonExistingNickname)).isFalse();
                    });
        }
    }

    @Nested
    @DisplayName("회원 정보 업데이트")
    class Update {

        @Test
        @DisplayName("마지막 로그인 시간 업데이트 성공")
        void updateLastLoginAt_Success() {
            // given
            Long memberId = 1L;
            LocalDateTime beforeUpdate = memberRepository.findById(memberId)
                    .map(MemberVO::getLastLoginAt)
                    .orElse(LocalDateTime.MIN);

            // when
            memberRepository.updateLastLoginAt(memberId);

            // then
            LocalDateTime afterUpdate = memberRepository.findById(memberId)
                    .map(MemberVO::getLastLoginAt)
                    .orElse(null);

            assertThat(afterUpdate)
                    .isNotNull()
                    .isAfter(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("권한 확인")
    class CheckRole {

        @Test
        @DisplayName("권한 확인 테스트")
        void hasRole_Success() {
            // given
            Long userId = 1L;
            Long adminId = 2L;

            // then
            assertThat(memberRepository)
                    .satisfies(repo -> {
                        assertThat(repo.hasRole(userId, "ROLE_USER")).isTrue();
                        assertThat(repo.hasRole(userId, "ROLE_ADMIN")).isFalse();
                        assertThat(repo.hasRole(adminId, "ROLE_ADMIN")).isTrue();
                    });
        }
    }

    private MemberVO createMemberVO(String loginId, Long roleId) {
        return MemberVO.builder()
                .loginId(loginId)
                .password("encodedPassword")
                .nickname("신규사용자")
                .roleId(roleId)
                .deleted(false)
                .build();
    }
}