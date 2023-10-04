package com.heavens.stream.repositories;

import com.heavens.stream.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findFirstByUsernameOrEmailAndActiveTrue(String username, String email);

    @Query(value = "SELECT au.*,a.role_name, a.id as role_id\n" +
            "    FROM users au\n" +
            "             LEFT JOIN user_authority ua ON au.id = ua.user_id\n" +
            "             LEFT JOIN authorities a ON ua.authority_id = a.id\n" +
            "    WHERE (LOWER(au.username) = LOWER(:username) OR LOWER(au.email) = LOWER(:email))\n" +
            "      AND au.active = true", nativeQuery = true)
    //@EntityGraph(attributePaths = "authorities")
    Optional<MyUser> findFirstByUsernameIgnoreCaseOrEmailIgnoreCaseAndActiveTrue(String username, String email);
}
