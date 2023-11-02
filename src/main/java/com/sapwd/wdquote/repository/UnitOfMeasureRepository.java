package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UnitOfMeasure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {
    Page<UnitOfMeasure> findAllByDeletedIsFalse(Pageable pageable);
}
