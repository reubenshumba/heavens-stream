package com.heavens.stream.repositories;

import com.heavens.stream.models.Authority;
import com.heavens.stream.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    // You can add custom queries here if needed
    Optional<Authority> findAllByMyUsers(MyUser users);
    Optional<Authority> findFirstByRoleNameOrAuthorityCodeAndActiveTrue(String roleName, String Code);

    List<Authority> findByRoleNameContainingIgnoreCase(String name);

    Optional<Authority> findByAuthorityCode(String authorityCode);
    Optional<Authority> findFirstByStringIdIgnoreCaseOrRoleNameIgnoreCase(String stringId, String roleName);

}