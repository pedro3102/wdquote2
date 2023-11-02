package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.SystemModelDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.SystemModel}.
 */
public interface SystemModelService {
    /**
     * Save a systemModel.
     *
     * @param systemModelDTO the entity to save.
     * @return the persisted entity.
     */
    SystemModelDTO save(SystemModelDTO systemModelDTO);

    /**
     * Updates a systemModel.
     *
     * @param systemModelDTO the entity to update.
     * @return the persisted entity.
     */
    SystemModelDTO update(SystemModelDTO systemModelDTO);

    /**
     * Partially updates a systemModel.
     *
     * @param systemModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemModelDTO> partialUpdate(SystemModelDTO systemModelDTO);

    /**
     * Get all the systemModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemModelDTO> findOne(Long id);

    /**
     * Delete the "id" systemModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SystemModelDTO> findAllBasic();
}
