package com.hh.multiboarduserbackend.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
public record PasswordRequestDto (
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String inputPassword
) {

}
