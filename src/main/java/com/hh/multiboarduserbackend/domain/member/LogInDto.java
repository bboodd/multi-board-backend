package com.hh.multiboarduserbackend.domain.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LogInDto(
          @NotBlank(message = "아이디를 입력해 주세요.")
          String loginId
        , @NotBlank(message = "비밀번호를 입력해 주세요.")
          String password
) {
}
