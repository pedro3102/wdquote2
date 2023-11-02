package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.Systema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Systema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemaRepository extends JpaRepository<Systema, Long> {
    Page<Systema> findAllByDeletedIsFalse(Pageable pageable);
}
