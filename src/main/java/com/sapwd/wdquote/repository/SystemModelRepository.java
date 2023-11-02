package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.SystemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemModelRepository extends JpaRepository<SystemModel, Long>, JpaSpecificationExecutor<SystemModel> {
    Page<SystemModel> findAllByDeletedIsFalse(Pageable pageable);
}
