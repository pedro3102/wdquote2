package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing AuthItem.
 */
public interface AuthorizationService {
    /**
     * Return all authorization for the action, the result will be cached.
     *
     * @param action The action.
     * @return All the authorizations.
     */
    Optional<ActionDTO> findActionAuthorization(String action);

    /**
     * Check if a Role has authorization for the action.
     *
     * @param action The action.
     * @param roles  The Roles set.
     * @return All the authorizations.
     */
    boolean hasActionAuthorization(String action, Set<String> roles);

    /**
     * Return all authorization to the resource, the result will be cached.
     *
     * @param resource The resource.
     * @return All the authorizations.
     */
    Optional<ResourceDTO> findResourceAuthorization(String resource);
}
