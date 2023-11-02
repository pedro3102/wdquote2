package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the Movement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, JpaSpecificationExecutor<Movement> {
    Movement findTop1ByCompletedAndNoStartsWithOrderByCreatedDateDesc(boolean completed, String format);

    @Query("select max(movement.consecutive) from Movement movement")
    @Transactional
    Long findMaxConsecutive();
}
