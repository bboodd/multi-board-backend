package com.hh.multiboarduserbackend.domain.member;

import lombok.Builder;

@Builder
public record DuplicateCheckRequestDto(
        String loginId
) {
}
