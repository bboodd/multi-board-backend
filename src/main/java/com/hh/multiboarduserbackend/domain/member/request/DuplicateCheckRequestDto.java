package com.hh.multiboarduserbackend.domain.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DuplicateCheckRequestDto(
        @NotBlank(message = "아이디를 입력해 주세요.")
        String loginId
) {
}
