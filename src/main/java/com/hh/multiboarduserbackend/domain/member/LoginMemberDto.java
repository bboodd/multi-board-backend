package com.hh.multiboarduserbackend.domain.member;

import lombok.Builder;

@Builder
public record LoginMemberDto(
        String loginId
) {
}
