package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.MovementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.Movement}.
 */
public interface MovementService {
    /**
     * Save a movement.
     *
     * @param movementDTO the entity to save.
     * @return the persisted entity.
     */
    MovementDTO save(MovementDTO movementDTO);

    /**
     * Updates a movement.
     *
     * @param movementDTO the entity to update.
     * @return the persisted entity.
     */
    MovementDTO update(MovementDTO movementDTO);

    /**
     * Partially updates a movement.
     *
     * @param movementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovementDTO> partialUpdate(MovementDTO movementDTO);

    /**
     * Get all the movements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MovementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" movement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovementDTO> findOne(Long id);

    /**
     * Delete the "id" movement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteDetails(Long id);

    MovementDTO confirmMovement(Long id) throws Exception;
}
