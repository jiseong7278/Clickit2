package com.project.clickit.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomMemberDetails implements UserDetails {

    private final MemberEntity memberEntity;

    public CustomMemberDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(memberEntity.getType()));
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
