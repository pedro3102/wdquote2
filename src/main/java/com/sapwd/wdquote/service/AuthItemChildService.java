package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.AuthItemDTO;

/**
 * Service Interface for managing AuthItemChild.
 */
public interface AuthItemChildService {
    /**
     * Toggle action status for a parent by ID.
     *
     * @param parentId  The parent ID.
     * @param actionDTO The actionDTO (AuthItemDTO).
     * @return The actionDTO toggled
     */
    AuthItemDTO toggleActionStatus(Long parentId, AuthItemDTO actionDTO);
}
