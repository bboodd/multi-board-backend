package com.spring.multiboardbackend.domain.member.repository;

import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Mapper
public interface MemberRepository {

    /**
     * 회원 저장
     */
    void save(MemberVO member);

    /**
     * 회원 ID로 조회
     */
    Optional<MemberVO> findById(Long id);

    /**
     * 로그인 ID로 회원 조회
     */
    Optional<MemberVO> findByLoginId(String loginId);

    /**
     * 로그인 ID 중복 확인
     */
    boolean existsByLoginId(String loginId);

    /**
     * 닉네임 중복 확인
     */
    boolean existsByNickname(String nickname);

    /**
     * 마지막 로그인 시간 업데이트
     */
    void updateLastLoginAt(Long id);

    /**
     * 회원 권한 확인
     */
    boolean hasRole(
            @Param("memberId") Long memberId,
            @Param("roleName") String roleName
    );
}
