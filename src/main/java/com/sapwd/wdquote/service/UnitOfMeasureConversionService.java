package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.UnitOfMeasureConversion}.
 */
public interface UnitOfMeasureConversionService {
    /**
     * Save a unitOfMeasureConversion.
     *
     * @param unitOfMeasureConversionDTO the entity to save.
     * @return the persisted entity.
     */
    UnitOfMeasureConversionDTO save(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO);

    /**
     * Updates a unitOfMeasureConversion.
     *
     * @param unitOfMeasureConversionDTO the entity to update.
     * @return the persisted entity.
     */
    UnitOfMeasureConversionDTO update(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO);

    /**
     * Partially updates a unitOfMeasureConversion.
     *
     * @param unitOfMeasureConversionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UnitOfMeasureConversionDTO> partialUpdate(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO);

    /**
     * Get all the unitOfMeasureConversions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UnitOfMeasureConversionDTO> findAll(Pageable pageable);

    /**
     * Get all the unitOfMeasureConversions by unitOfMeasure.
     *
     * @param id the id of the Unit Of Measure.
     * @return the list of entities.
     */
    List<UnitOfMeasureConversionDTO> findAllByUnitOfMeasureId(Long id);

    /**
     * Get the "id" unitOfMeasureConversion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitOfMeasureConversionDTO> findOne(Long id);

    /**
     * Delete the "id" unitOfMeasureConversion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
