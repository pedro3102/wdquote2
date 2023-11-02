package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.TaxAreaCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.TaxAreaCode}.
 */
public interface TaxAreaCodeService {
    /**
     * Save a taxAreaCode.
     *
     * @param taxAreaCodeDTO the entity to save.
     * @return the persisted entity.
     */
    TaxAreaCodeDTO save(TaxAreaCodeDTO taxAreaCodeDTO);

    /**
     * Updates a taxAreaCode.
     *
     * @param taxAreaCodeDTO the entity to update.
     * @return the persisted entity.
     */
    TaxAreaCodeDTO update(TaxAreaCodeDTO taxAreaCodeDTO);

    /**
     * Partially updates a taxAreaCode.
     *
     * @param taxAreaCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaxAreaCodeDTO> partialUpdate(TaxAreaCodeDTO taxAreaCodeDTO);

    /**
     * Get all the taxAreaCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxAreaCodeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taxAreaCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxAreaCodeDTO> findOne(Long id);

    /**
     * Delete the "id" taxAreaCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
