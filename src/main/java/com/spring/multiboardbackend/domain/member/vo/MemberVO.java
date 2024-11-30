package com.spring.multiboardbackend.domain.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberVO {
    private Long id;
    private Long roleId;
    private String loginId;

    @JsonIgnore
    private String password;

    private String nickname;
    private boolean deleted;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
