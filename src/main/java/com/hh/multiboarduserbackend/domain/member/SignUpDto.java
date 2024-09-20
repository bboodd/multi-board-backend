package com.hh.multiboarduserbackend.domain.member;

import com.hh.multiboarduserbackend.exception.MemberErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpDto (
          @NotBlank(message = "아이디를 입력해 주세요")
          @Pattern(regexp = "^[a-zA-Z0-9_-]{4,12}$", message = "아이디는 '-','_'외 특수문자를 제외한 4자리 이상 12자리 미만 영문또는 숫자이어야 합니다")
          String loginId
        ,
          @Pattern(regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,16}$", message = "비밀번호는 4글자 이상, 16글자 미만인 영문/숫자/특수문자를 포함한 문자여야 합니다.")
          String password
        , @Pattern(regexp = "^(?!.*([a-zA-Z0-9#?!@$ %^&*-])\\1{2}).*$", message = "비밀번호에 동일만 문자 혹은 숫자는 3회 이상 반복할 수 없습니다.")
          String checkPassword
        , @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,10}$", message = "이름은 2글자 ~ 10글자의 영문/숫자/한글 이어야 합니다.")
          String nickname
) {

    public SignUpDto {
        if(!password.equals(checkPassword)) {
            throw MemberErrorCode.SIGN_UP_PASSWORD_CHECK_ERROR.defaultException();
        }
        if(loginId.equals(password)) {
            throw MemberErrorCode.ID_PASSWORD_EQUALS_ERROR.defaultException();
        }
    }
}
