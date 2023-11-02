package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.UnitOfMeasureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.UnitOfMeasure}.
 */
public interface UnitOfMeasureService {
    /**
     * Save a unitOfMeasure.
     *
     * @param unitOfMeasureDTO the entity to save.
     * @return the persisted entity.
     */
    UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO);

    /**
     * Updates a unitOfMeasure.
     *
     * @param unitOfMeasureDTO the entity to update.
     * @return the persisted entity.
     */
    UnitOfMeasureDTO update(UnitOfMeasureDTO unitOfMeasureDTO);

    /**
     * Partially updates a unitOfMeasure.
     *
     * @param unitOfMeasureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UnitOfMeasureDTO> partialUpdate(UnitOfMeasureDTO unitOfMeasureDTO);

    /**
     * Get all the unitOfMeasures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UnitOfMeasureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" unitOfMeasure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitOfMeasureDTO> findOne(Long id);

    /**
     * Delete the "id" unitOfMeasure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
