package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.Movement;
import com.sapwd.wdquote.domain.MovementDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MovementDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovementDetailsRepository extends JpaRepository<MovementDetails, Long>, JpaSpecificationExecutor<MovementDetails> {
    List<MovementDetails> findAllByMovement(Movement movement);
}
