package com.heavens.stream.repositories;

import com.heavens.stream.models.Authority;
import com.heavens.stream.models.Heaven;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface HeavenRepository extends JpaRepository<Heaven, Long> {

    Page<Heaven> findAllByActiveTrue(Pageable pageable);
    Page<Heaven> findDistinctByAuthoritiesInOrHeavenOwnAndActiveTrue(List<Authority> authorities,Long heavenOwn, Pageable pageable);
    Optional<Heaven> findFirstByHeavenNameOrStringId(String heavenName, String stringId);
    Page<Heaven> findDistinctByHeavenNameContainingIgnoreCaseAndAuthoritiesInOrHeavenOwnAndActiveTrue(String keyword, List<Authority> authorities, Long heavenOwn, Pageable pageable);
    Page<Heaven> findByHeavenNameContainingIgnoreCaseAndActiveTrue(String keyword, Pageable pageable);
}

