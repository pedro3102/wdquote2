package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.SystemaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.Systema}.
 */
public interface SystemaService {
    /**
     * Save a systema.
     *
     * @param systemaDTO the entity to save.
     * @return the persisted entity.
     */
    SystemaDTO save(SystemaDTO systemaDTO);

    /**
     * Updates a systema.
     *
     * @param systemaDTO the entity to update.
     * @return the persisted entity.
     */
    SystemaDTO update(SystemaDTO systemaDTO);

    /**
     * Partially updates a systema.
     *
     * @param systemaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemaDTO> partialUpdate(SystemaDTO systemaDTO);

    /**
     * Get all the systemas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemaDTO> findAll(Pageable pageable);

    /**
     * Get all the systems with basic info.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemaDTO> findAllBasic(Pageable pageable);

    /**
     * Get the "id" systema.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemaDTO> findOne(Long id);

    /**
     * Delete the "id" systema.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
