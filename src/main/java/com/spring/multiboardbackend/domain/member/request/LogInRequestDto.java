package com.spring.multiboardbackend.domain.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LogInRequestDto(
          @NotBlank(message = "아이디를 입력해 주세요.")
          String loginId
        , @NotBlank(message = "비밀번호를 입력해 주세요.")
          String password
) {
}
