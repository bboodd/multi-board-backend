package com.hh.multiboarduserbackend.domain.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DuplicateCheckRequestDto(
        @NotBlank(message = "아이디 혹은 닉네임을 입력해 주세요.")
        String str
) {
}
