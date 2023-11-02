package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.CustomerContactDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sapwd.wdquote.domain.CustomerContact}.
 */
public interface CustomerContactService {
    /**
     * Save a customerContact.
     *
     * @param customerContactDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerContactDTO save(CustomerContactDTO customerContactDTO);

    /**
     * Updates a customerContact.
     *
     * @param customerContactDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerContactDTO update(CustomerContactDTO customerContactDTO);

    /**
     * Partially updates a customerContact.
     *
     * @param customerContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerContactDTO> partialUpdate(CustomerContactDTO customerContactDTO);

    /**
     * Get all the customerContacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerContactDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customerContact.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerContactDTO> findOne(Long id);

    /**
     * Delete the "id" customerContact.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
