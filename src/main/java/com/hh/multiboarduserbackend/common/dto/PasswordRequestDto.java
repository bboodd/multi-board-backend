package com.hh.multiboarduserbackend.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    String inputPassword;
}
