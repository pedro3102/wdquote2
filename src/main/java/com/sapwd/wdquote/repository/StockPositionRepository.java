package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.StockPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the StockPosition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockPositionRepository extends JpaRepository<StockPosition, Long> {
    List<StockPosition> findAllByDeletedIsFalse();
}
