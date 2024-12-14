package com.spring.multiboardbackend.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(1L, "ROLE_USER"),
    ADMIN(2L, "ROLE_ADMIN");

    private final Long id;
    private final String roleName;


    public static Role fromId(Long id) {
        for (Role role : values()) {
            if (Objects.equals(role.id, id)) {
                return role;
            }
        }
        // 매칭되지 않을 경우 기본 USER 혹은 예외 처리
        return USER;
    }
}
