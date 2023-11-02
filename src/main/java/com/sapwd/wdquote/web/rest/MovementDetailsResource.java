package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.MovementDetailsRepository;
import com.sapwd.wdquote.service.MovementDetailsQueryService;
import com.sapwd.wdquote.service.MovementDetailsService;
import com.sapwd.wdquote.service.criteria.MovementDetailsCriteria;
import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sapwd.wdquote.domain.MovementDetails}.
 */
@RestController
@RequestMapping("/api")
public class MovementDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MovementDetailsResource.class);

    private static final String ENTITY_NAME = "movementDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovementDetailsService movementDetailsService;

    private final MovementDetailsRepository movementDetailsRepository;

    private final MovementDetailsQueryService movementDetailsQueryService;

    public MovementDetailsResource(
        MovementDetailsService movementDetailsService,
        MovementDetailsRepository movementDetailsRepository,
        MovementDetailsQueryService movementDetailsQueryService
    ) {
        this.movementDetailsService = movementDetailsService;
        this.movementDetailsRepository = movementDetailsRepository;
        this.movementDetailsQueryService = movementDetailsQueryService;
    }

    /**
     * {@code POST  /movement-details} : Create a new movementDetails.
     *
     * @param movementDetailsDTO the movementDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movementDetailsDTO, or with status {@code 400 (Bad Request)} if the movementDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movement-details")
    public ResponseEntity<MovementDetailsDTO> createMovementDetails(@Valid @RequestBody MovementDetailsDTO movementDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save MovementDetails : {}", movementDetailsDTO);
        if (movementDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new movementDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovementDetailsDTO result = movementDetailsService.save(movementDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/movement-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movement-details/:id} : Updates an existing movementDetails.
     *
     * @param id the id of the movementDetailsDTO to save.
     * @param movementDetailsDTO the movementDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the movementDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movementDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movement-details/{id}")
    public ResponseEntity<MovementDetailsDTO> updateMovementDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MovementDetailsDTO movementDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MovementDetails : {}, {}", id, movementDetailsDTO);
        if (movementDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovementDetailsDTO result = movementDetailsService.update(movementDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movement-details/:id} : Partial updates given fields of an existing movementDetails, field will ignore if it is null
     *
     * @param id the id of the movementDetailsDTO to save.
     * @param movementDetailsDTO the movementDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the movementDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movementDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movementDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movement-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovementDetailsDTO> partialUpdateMovementDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MovementDetailsDTO movementDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MovementDetails partially : {}, {}", id, movementDetailsDTO);
        if (movementDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovementDetailsDTO> result = movementDetailsService.partialUpdate(movementDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /movement-details} : get all the movementDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movementDetails in body.
     */
    @GetMapping("/movement-details")
    public ResponseEntity<List<MovementDetailsDTO>> getAllMovementDetails(
        MovementDetailsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MovementDetails by criteria: {}", criteria);
        Page<MovementDetailsDTO> page = movementDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movement-details/count} : count all the movementDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/movement-details/count")
    public ResponseEntity<Long> countMovementDetails(MovementDetailsCriteria criteria) {
        log.debug("REST request to count MovementDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(movementDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /movement-details/:id} : get the "id" movementDetails.
     *
     * @param id the id of the movementDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movementDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movement-details/{id}")
    public ResponseEntity<MovementDetailsDTO> getMovementDetails(@PathVariable Long id) {
        log.debug("REST request to get MovementDetails : {}", id);
        Optional<MovementDetailsDTO> movementDetailsDTO = movementDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movementDetailsDTO);
    }

    /**
     * {@code DELETE  /movement-details/:id} : delete the "id" movementDetails.
     *
     * @param id the id of the movementDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movement-details/{id}")
    public ResponseEntity<Void> deleteMovementDetails(@PathVariable Long id) {
        log.debug("REST request to delete MovementDetails : {}", id);
        movementDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
