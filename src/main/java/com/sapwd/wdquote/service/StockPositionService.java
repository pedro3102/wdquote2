package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.StockPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.StockPosition}.
 */
public interface StockPositionService {
    /**
     * Save a stockPosition.
     *
     * @param stockPositionDTO the entity to save.
     * @return the persisted entity.
     */
    StockPositionDTO save(StockPositionDTO stockPositionDTO);

    /**
     * Updates a stockPosition.
     *
     * @param stockPositionDTO the entity to update.
     * @return the persisted entity.
     */
    StockPositionDTO update(StockPositionDTO stockPositionDTO);

    /**
     * Partially updates a stockPosition.
     *
     * @param stockPositionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockPositionDTO> partialUpdate(StockPositionDTO stockPositionDTO);

    /**
     * Get all the stockPositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockPositionDTO> findAll(Pageable pageable);

    /**
     * Get all the stockPositions basic.
     *
     * @return the list of entities.
     */
    List<StockPositionDTO> findAllBasic();

    /**
     * Get the "id" stockPosition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockPositionDTO> findOne(Long id);

    /**
     * Delete the "id" stockPosition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
