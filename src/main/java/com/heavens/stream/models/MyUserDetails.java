package com.heavens.stream.models;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class MyUserDetails implements UserDetails {
    private final MyUser myUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("find all authorities ");
        return myUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return myUser.getPassword();
    }

    @Override
    public String getUsername() {
        return myUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return myUser.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return myUser.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return myUser.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return myUser.isActive();
    }

    public MyUser getMyUser() {
        return this.myUser;
    }


}
