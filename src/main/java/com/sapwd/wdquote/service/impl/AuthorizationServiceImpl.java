package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.security.SecurityUtils;
import com.sapwd.wdquote.service.AuthItemService;
import com.sapwd.wdquote.service.AuthorizationService;
import com.sapwd.wdquote.service.api.CacheFactory;
import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AuthItem.
 */
@Service
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {

    private final Logger log = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private final CacheFactory authCacheFactory;
    private final AuthItemService authItemService;

    public AuthorizationServiceImpl(CacheFactory authCacheFactory, AuthItemService authItemService) {
        this.authCacheFactory = authCacheFactory;
        this.authItemService = authItemService;
    }

    /**
     * Return all authorization for the action, the result will be cached.
     *
     * @param action The action.
     * @return All the authorizations.
     */
    @Override
    public Optional<ActionDTO> findActionAuthorization(String action) {
        if (!action.isEmpty()) {
            var authsOptional = authCacheFactory.getActionCache().get(action);
            if (authsOptional.isEmpty()) { // If not found in Cache get from DB and store in cache for future needs.
                var actionOptional = authItemService.actionAuthorization(action);
                actionOptional.ifPresent(actionAuth -> authCacheFactory.getActionCache().put(action, actionAuth));
                return actionOptional;
            }
            return authsOptional;
        }
        return Optional.empty();
    }

    /**
     * Check if a Role has authorization for the action.
     *
     * @param action The action.
     * @param roles  The Roles set.
     * @return All the authorizations.
     */
    @Override
    public boolean hasActionAuthorization(String action, Set<String> roles) {
        var actionOptional = findActionAuthorization(action);
        if (actionOptional.isPresent()) {
            Set<String> intersection = new HashSet<>(actionOptional.get().getRoles());
            intersection.retainAll(roles);
            return !intersection.isEmpty();
        }
        throw new RuntimeException("Action not found: " + action);
    }

    /**
     * Return all authorization to the resource, the result will be cached.
     *
     * @param resourceName The resource name.
     * @return All the authorizations.
     */
    @Override
    public Optional<ResourceDTO> findResourceAuthorization(String resourceName) {
        if (!resourceName.isEmpty()) {
            var cacheKey = buildKeyName(resourceName);
            var authsOptional = authCacheFactory.getResourceCache().get(cacheKey);
            if (authsOptional.isEmpty()) { // If not found in Cache get from DB and store in cache for future needs.
                var resourceOptional = authItemService.resourceAuthorization(resourceName);
                resourceOptional.ifPresent(resourceAction -> authCacheFactory.getResourceCache().put(cacheKey, resourceAction));
                return resourceOptional;
            }
            return authsOptional;
        }
        return Optional.empty();
    }

    private String buildKeyName(String resourceName) {
        Optional<String> optional = SecurityUtils.getCurrentUserLogin();
        return optional.map(s -> s.toUpperCase() + "-" + resourceName).orElse(resourceName);
    }
}
