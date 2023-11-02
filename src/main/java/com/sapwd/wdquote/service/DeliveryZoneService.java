package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.DeliveryZoneDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.DeliveryZone}.
 */
public interface DeliveryZoneService {
    /**
     * Save a deliveryZone.
     *
     * @param deliveryZoneDTO the entity to save.
     * @return the persisted entity.
     */
    DeliveryZoneDTO save(DeliveryZoneDTO deliveryZoneDTO);

    /**
     * Updates a deliveryZone.
     *
     * @param deliveryZoneDTO the entity to update.
     * @return the persisted entity.
     */
    DeliveryZoneDTO update(DeliveryZoneDTO deliveryZoneDTO);

    /**
     * Partially updates a deliveryZone.
     *
     * @param deliveryZoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeliveryZoneDTO> partialUpdate(DeliveryZoneDTO deliveryZoneDTO);

    /**
     * Get all the deliveryZones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliveryZoneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deliveryZone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeliveryZoneDTO> findOne(Long id);

    /**
     * Delete the "id" deliveryZone.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
