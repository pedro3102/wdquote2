package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.service.AuthorizationService;
import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Authorization.
 */
@RestController
@RequestMapping("/api")
public class AuthorizationResource {

    private final AuthorizationService authorizationService;

    private final Logger log = LoggerFactory.getLogger(AuthorizationResource.class);

    public AuthorizationResource(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * GET  /authorization/:action/access : get the action authorization.
     *
     * @param action the action name
     * @return the ResponseEntity with status 200 (OK) and with body the authItem, or with status 404 (Not Found)
     */
    @GetMapping("/authorization/{action}/action")
    public ResponseEntity<ActionDTO> getActionAuthorization(@PathVariable String action) {
        log.debug("REST request to get AuthItem : {}", action);
        return ResponseUtil.wrapOrNotFound(authorizationService.findActionAuthorization(action));
    }

    /**
     * GET  /authorization/:resource/resource : get the resource authorization.
     *
     * @param resource the resource name
     * @return the ResponseEntity with status 200 (OK) and with body the authItem, or with status 404 (Not Found)
     */
    @GetMapping("/authorization/{resource}/resource")
    public ResponseEntity<ResourceDTO> getResourceAuthorization(@PathVariable String resource) {
        log.debug("REST request to get AuthItem : {}", resource);
        return ResponseUtil.wrapOrNotFound(authorizationService.findResourceAuthorization(resource));
    }
}
