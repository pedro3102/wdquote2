package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.MovementType;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MovementType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovementTypeRepository extends JpaRepository<MovementType, Long> {
    List<MovementType> findAllByTypeAndDeletedIsFalse(OperationType type);

    List<MovementType> findAllByDeletedIsFalse();
}
