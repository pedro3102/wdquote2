package com.sapwd.wdquote.web.rest.admin;

import com.sapwd.wdquote.repository.AuthItemRepository;
import com.sapwd.wdquote.service.RoleService;
import com.sapwd.wdquote.service.criteria.AuthItemCriteria;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Roles.
 */
@RestController
@RequestMapping("/api/admin")
public class ManageRoleResource {

    private final Logger log = LoggerFactory.getLogger(ManageRoleResource.class);
    private static final String ENTITY_NAME = "role";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;
    private final AuthItemRepository authItemRepository;

    public ManageRoleResource(RoleService roleService, AuthItemRepository authItemRepository) {
        this.roleService = roleService;
        this.authItemRepository = authItemRepository;
    }

    /**
     * {@code POST  /roles} : Create a new role.
     *
     * @param roleDTO the roleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleDTO,
     * or with status {@code 400 (Bad Request)} if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<AuthItemDTO> createRole(@Valid @RequestBody AuthItemDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDTO);
        if (roleDTO.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthItemDTO result = roleService.saveRole(roleDTO);
        return ResponseEntity
            .created(new URI("/api/cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roles/:id} : Partial updates given fields of an existing role,
     * field will ignore if it is null
     *
     * @param id          the id of the authItemDTO to save.
     * @param authItemDTO the authItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authItemDTO,
     * or with status {@code 400 (Bad Request)} if the authItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authItemDTO couldn't be updated.
     */
    @PatchMapping(value = "/roles/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<AuthItemDTO> partialUpdateRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuthItemDTO authItemDTO
    ) {
        log.debug("REST request to partial update Role partially: {}, {}", id, authItemDTO);
        if (authItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!authItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<AuthItemDTO> result = roleService.partialUpdateRole(authItemDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authItemDTO.getId().toString())
        );
    }

    /**
     * GET  /roles : get all Roles.
     * <p>
     * Allowed types:
     * <p>
     * 1: Roles
     * 2: Resource
     * 3: Action
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of Roles in body
     */
    @GetMapping("/roles")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<List<AuthItemDTO>> getAllRoles(AuthItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Roles by criteria: {}", criteria);
        var page = roleService.findAllRolesByCriteria(criteria, pageable);
        var headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /roles/:id : get the "id" Role.
     *
     * @param id the id of the Role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authItem, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<AuthItemDTO> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<AuthItemDTO> authItem = roleService.findRole(id);
        return ResponseUtil.wrapOrNotFound(authItem);
    }

    /**
     * {@code DELETE  /roles/:id} : delete the "id" role.
     *
     * @param id the id of the roleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roles/{id}")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleService.deleteRole(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /roles/:id/actions : get all actions for a role.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of Actions for a Role in body
     */
    @GetMapping("/roles/{id}/actions")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<List<AuthItemDTO>> getAllRoleActions(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get Actions for a Role: {}", id);
        var result = roleService.findAllChild(id, pageable);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /roles/{id}/actions} : Updates an existing action.
     *
     * @param id          the id of the authItemDTO to save.
     * @param authItemDTO the authItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authItemDTO,
     * or with status {@code 400 (Bad Request)} if the authItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authItemDTO couldn't be updated.
     */
    @PutMapping("/roles/{id}/actions")
    public ResponseEntity<AuthItemDTO> toggleRoleAction(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody AuthItemDTO authItemDTO
    ) {
        log.debug("REST request to toggle Action for a Role : {}, {}", id, authItemDTO);
        if (authItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuthItemDTO result = roleService.toggleActionStatus(id, authItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authItemDTO.getId().toString()))
            .body(result);
    }

    @PostMapping("/roles/ops/batch")
    @PreAuthorize("@userAuthorizationService.hasAccessToManage()")
    public ResponseEntity<List<AuthItemDTO>> loadData(@RequestBody List<AuthItemDTO> roles) throws URISyntaxException {
        log.debug("REST request to save a roles list: {}", roles);
        var result = roleService.saveAllRoles(roles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roles.toString()))
            .body(result);
    }
}
