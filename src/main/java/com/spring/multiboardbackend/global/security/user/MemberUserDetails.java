package com.spring.multiboardbackend.global.security.user;

import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class MemberUserDetails implements UserDetails {

    private final MemberVO member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = Role.fromId(member.getRoleId());
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !member.isDeleted();
    }

}
