package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.TaxAreaCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxAreaCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxAreaCodeRepository extends JpaRepository<TaxAreaCode, Long> {
    Page<TaxAreaCode> findAllByDeletedIsFalse(Pageable pageable);
}
