package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.service.RoleService;
import com.sapwd.wdquote.service.criteria.AuthItemCriteria;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing AuthItem.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);
    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
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
    public ResponseEntity<List<AuthItemDTO>> getAllRoles(Pageable pageable) {
        log.debug("REST request to get a page of Roles");
        Page<AuthItemDTO> page = roleService.findAllRolesByCriteria(new AuthItemCriteria(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
