package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.repository.AuthItemRepository;
import com.sapwd.wdquote.security.AuthoritiesConstants;
import com.sapwd.wdquote.security.AuthorityType;
import com.sapwd.wdquote.security.SecurityUtils;
import com.sapwd.wdquote.service.AuthItemChildService;
import com.sapwd.wdquote.service.AuthItemQueryService;
import com.sapwd.wdquote.service.AuthItemService;
import com.sapwd.wdquote.service.RoleService;
import com.sapwd.wdquote.service.api.CacheFactory;
import com.sapwd.wdquote.service.criteria.AuthItemCriteria;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.service.mapper.AuthItemMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final AuthItemRepository authItemRepository;
    private final AuthItemMapper authItemMapper;
    private final AuthItemService authItemService;
    private final AuthItemChildService authItemChildService;
    private final AuthItemQueryService authItemQueryService;
    private final CacheFactory authCacheFactory;

    public RoleServiceImpl(
        AuthItemRepository authItemRepository,
        AuthItemMapper authItemMapper,
        AuthItemService authItemService,
        AuthItemChildService authItemChildService,
        AuthItemQueryService authItemQueryService,
        CacheFactory authCacheFactory
    ) {
        this.authItemRepository = authItemRepository;
        this.authItemMapper = authItemMapper;
        this.authItemService = authItemService;
        this.authItemChildService = authItemChildService;
        this.authItemQueryService = authItemQueryService;
        this.authCacheFactory = authCacheFactory;
    }

    /**
     * Save a Role.
     *
     * @param authItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AuthItemDTO saveRole(AuthItemDTO authItemDTO) {
        log.debug("Request to save Role: {}", authItemDTO);
        return buildRole(authItemDTO);
    }

    @Override
    public List<AuthItemDTO> saveAllRoles(List<AuthItemDTO> roles) {
        log.debug("Request to save roles: {}", roles);
        return roles.stream().map(this::saveRole).collect(Collectors.toList());
    }

    /**
     * Get all Roles.
     *
     * @return all entities
     */
    @Override
    public Page<AuthItemDTO> findAllRolesByCriteria(AuthItemCriteria criteria, Pageable pageable) {
        log.debug("Request to get all AuthItems Roles");
        IntegerFilter typeFilter = new IntegerFilter(); // Set Role type filter
        typeFilter.setEquals(AuthorityType.ROLE);
        criteria.setType(typeFilter);
        List<Long> defaultRoles = new ArrayList<>();
        defaultRoles.add(AuthoritiesConstants.ROLE_ADMIN_ID);
        defaultRoles.add(AuthoritiesConstants.ROLE_USER_ID);
        if (SecurityUtils.isCurrentUserSystem()) { // Add ROLE_SUPER_ADMIN if current user has ROLE_SUPER_ADMIN
            defaultRoles.add(AuthoritiesConstants.ROLE_SYSTEM_ID);
        }
        LongFilter idFilter = new LongFilter();
        idFilter.setIn(defaultRoles); // Set default Roles filter
        criteria.setGlobalId(idFilter);
        return authItemQueryService.findByCriteria(criteria, pageable);
    }

    /**
     * Get the "id" Role.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AuthItemDTO> findRole(Long id) {
        log.debug("Request to get Role by id");
        return authItemRepository.findRole(id).map(authItemMapper::toDto);
    }

    /**
     * Save a Role.
     *
     * @param authItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public Optional<AuthItemDTO> partialUpdateRole(AuthItemDTO authItemDTO) {
        log.debug("Request to save Role : {}", authItemDTO);
        return authItemService.partialUpdate(authItemDTO);
    }

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    @Override
    public void deleteRole(Long id) {
        log.debug("Request to delete Role : {}", id);
        authItemService.delete(id);
    }

    /**
     * Fin all child for a Role.
     *
     * @param id       the id of the Resource.
     * @param pageable the pageable.
     * @return the persisted entity.
     */
    @Override
    public List<AuthItemDTO> findAllChild(Long id, Pageable pageable) {
        log.debug("Request to get all child for a Resource by id");
        return authItemMapper.toDto(authItemRepository.findAllChildren(id));
    }

    /**
     * Toggle action status for a role by ID.
     *
     * @param id        The role ID.
     * @param actionDTO The actionDTO (AuthItemDTO).
     * @return The actionDTO toggled
     */
    @Override
    public AuthItemDTO toggleActionStatus(Long id, AuthItemDTO actionDTO) {
        authCacheFactory.getResourceCache().clear();
        return authItemChildService.toggleActionStatus(id, actionDTO);
    }

    private AuthItemDTO buildRole(AuthItemDTO authItemDTO) {
        AuthItem authItem = authItemMapper.toEntity(authItemDTO);
        authItem.setType(AuthorityType.ROLE);
        authItem = authItemRepository.save(authItem);
        return authItemMapper.toDto(authItem);
    }
}
