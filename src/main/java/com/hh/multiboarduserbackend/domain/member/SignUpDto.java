package com.hh.multiboarduserbackend.domain.member;

import lombok.Builder;

@Builder
public record SignUpDto (
          String loginId
        , String password
        , String checkPassword
        , String nickname
) {
}
