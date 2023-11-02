package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import java.util.Optional;

/**
 * Service Interface for managing AuthItem.
 */
public interface AuthItemService {
    /**
     * Save a AuthItem.
     *
     * @param authItemDTO the entity to save
     * @return the persisted entity
     */
    Optional<AuthItemDTO> partialUpdate(AuthItemDTO authItemDTO);

    Optional<ActionDTO> actionAuthorization(String action);

    Optional<ResourceDTO> resourceAuthorization(String resource);

    /**
     * Delete the "id" authItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
