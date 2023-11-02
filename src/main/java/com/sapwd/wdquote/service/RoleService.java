package com.sapwd.wdquote.service;

import com.sapwd.wdquote.service.criteria.AuthItemCriteria;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Roles.
 */
public interface RoleService {
    /**
     * Save a Role.
     *
     * @param authItemDTO the entity to save.
     * @return the persisted entity.
     */
    AuthItemDTO saveRole(AuthItemDTO authItemDTO);

    List<AuthItemDTO> saveAllRoles(List<AuthItemDTO> roles);

    /**
     * Get all Roles using Criteria.
     *
     * @return all entities
     */
    Page<AuthItemDTO> findAllRolesByCriteria(AuthItemCriteria criteria, Pageable pageable);

    /**
     * Get the "id" Role.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AuthItemDTO> findRole(Long id);

    /**
     * Save a Role.
     *
     * @param authItemDTO the entity to save
     * @return the persisted entity
     */
    Optional<AuthItemDTO> partialUpdateRole(AuthItemDTO authItemDTO);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    void deleteRole(Long id);

    /**
     * Fin all child for a Role.
     *
     * @param id       the id of the Resource.
     * @param pageable the pageable.
     * @return the persisted entity.
     */
    List<AuthItemDTO> findAllChild(Long id, Pageable pageable);

    /**
     * Toggle action status for a role by ID.
     *
     * @param id        The role ID.
     * @param actionDTO The actionDTO (AuthItemDTO).
     * @return The actionDTO toggled
     */
    AuthItemDTO toggleActionStatus(Long id, AuthItemDTO actionDTO);
}
