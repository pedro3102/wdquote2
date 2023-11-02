package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.service.dto.MovementTypeDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.MovementType}.
 */
public interface MovementTypeService {
    /**
     * Save a movementType.
     *
     * @param movementTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MovementTypeDTO save(MovementTypeDTO movementTypeDTO);

    /**
     * Updates a movementType.
     *
     * @param movementTypeDTO the entity to update.
     * @return the persisted entity.
     */
    MovementTypeDTO update(MovementTypeDTO movementTypeDTO);

    /**
     * Partially updates a movementType.
     *
     * @param movementTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovementTypeDTO> partialUpdate(MovementTypeDTO movementTypeDTO);

    /**
     * Get all the movementTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MovementTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the movementTypes basic information.
     *
     * @param type the OperationType type.
     * @return the list of entities.
     */
    List<MovementTypeDTO> findAllBasic(OperationType type);

    /**
     * Get the "id" movementType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovementTypeDTO> findOne(Long id);

    /**
     * Delete the "id" movementType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
