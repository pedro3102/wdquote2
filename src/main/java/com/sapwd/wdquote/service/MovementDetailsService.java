package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.MovementDetails}.
 */
public interface MovementDetailsService {
    /**
     * Save a movementDetails.
     *
     * @param movementDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    MovementDetailsDTO save(MovementDetailsDTO movementDetailsDTO);

    /**
     * Updates a movementDetails.
     *
     * @param movementDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    MovementDetailsDTO update(MovementDetailsDTO movementDetailsDTO);

    /**
     * Partially updates a movementDetails.
     *
     * @param movementDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovementDetailsDTO> partialUpdate(MovementDetailsDTO movementDetailsDTO);

    /**
     * Get all the movementDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MovementDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" movementDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovementDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" movementDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
