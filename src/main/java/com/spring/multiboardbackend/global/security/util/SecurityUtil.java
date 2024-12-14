package com.spring.multiboardbackend.global.security.util;

import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.global.security.user.MemberUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentMemberId() {
        MemberVO member = getCurrentMember();

        return member.getId();
    }

    public String getCurrentMemberNickname() {
        MemberVO member = getCurrentMember();

        return member.getNickname();
    }

    private MemberVO getCurrentMember() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw MemberErrorCode.MEMBER_NOT_FOUND.defaultException();
        }

        if (!(authentication.getPrincipal() instanceof MemberUserDetails)) {
            throw MemberErrorCode.MEMBER_NOT_FOUND.defaultException();
        }

        MemberUserDetails userDetails = (MemberUserDetails) authentication.getPrincipal();
        MemberVO member = userDetails.getMember();

        if (member.getId() == null) {
            throw MemberErrorCode.MEMBER_NOT_FOUND.defaultException();
        }

        return member;
    }

}
