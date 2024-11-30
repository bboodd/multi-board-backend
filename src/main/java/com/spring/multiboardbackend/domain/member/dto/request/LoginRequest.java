package com.spring.multiboardbackend.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 아이디", example = "user123")
        @NotBlank(message = "아이디를 입력해 주세요.")
        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 사용 가능합니다.")
        String loginId,

        @Schema(description = "비밀번호", example = "password123!")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Size(min = 4, max = 16, message = "비밀번호는 4자 이상 16자 이하로 입력해 주세요.")
        String password
) {
    /**
     * 보안을 위해 toString 재정의
     */
    @Override
    public String toString() {
        return String.format("LoginRequest(loginId=%s, password=****)", loginId);
    }
}
