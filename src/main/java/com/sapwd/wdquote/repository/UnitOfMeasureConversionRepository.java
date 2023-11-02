package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.UnitOfMeasureConversion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UnitOfMeasureConversion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasureConversionRepository
    extends JpaRepository<UnitOfMeasureConversion, Long>, JpaSpecificationExecutor<UnitOfMeasureConversion> {
    @Query(
        "SELECT unitOfMeasureConversion FROM UnitOfMeasureConversion unitOfMeasureConversion" +
        " LEFT JOIN FETCH unitOfMeasureConversion.uom uom " +
        " WHERE unitOfMeasureConversion.deleted = false AND uom.id=:uomId"
    )
    List<UnitOfMeasureConversion> findAllByUnitOfMeasureId(@Param("uomId") Long uomId);
}
