package com.heavens.stream.helpers;

import com.heavens.stream.models.Authority;
import com.heavens.stream.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class AuthorityHelper {

    public static boolean hasSuperAdminAuthority(List<Authority> authorities) {
        return authorities.stream()
                .anyMatch(authority -> authority.getRoleName().equals("ROLE_SUPER_ADMIN"));
    }

    public static boolean hasAdminAuthority(List<Authority> authorities) {
        return authorities.stream()
                .anyMatch(authority -> authority.getRoleName().equals("ROLE_ADMIN"));
    }

    public static boolean hasSuperAdminAuthority(Optional<List<Authority>> userAuthoritiesOptional) {
        return userAuthoritiesOptional.map(AuthorityHelper::hasSuperAdminAuthority).orElse(false);
    }

    public static boolean hasAdminAuthority(Optional<List<Authority>> userAuthoritiesOptional) {
        return userAuthoritiesOptional.map(AuthorityHelper::hasAdminAuthority).orElse(false);
    }


    public static Authority createDefaultUserRole(AuthorityRepository authorityRepository) {
        createAdminAuthorities("ADMIN","9049");
        createAdminAuthorities("SUPER ADMIN", "1402");
        return AuthorityHelper.createAdminAuthorities("USER","14029049");
    }

    private static Authority createAdminAuthorities(String roleName,String code) {
        Authority authority = Authority
                .builder()
                .roleName("ROLE_" + roleName.toUpperCase().replaceAll("\\s+", "_")
                        .replaceAll("(_)+", "_"))
                .visible(false)
                .authorityCode(code)
                .roleDescription(roleName)
                .build();

        authority.setActive(true);
        authority.setCreatedBy(0L);
        authority.setUpdatedBy(0L);
        return authority;
    }


}
