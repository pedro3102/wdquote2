package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.MovementRepository;
import com.sapwd.wdquote.service.MovementQueryService;
import com.sapwd.wdquote.service.MovementService;
import com.sapwd.wdquote.service.criteria.MovementCriteria;
import com.sapwd.wdquote.service.dto.MovementDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.Movement}.
 */
@RestController
@RequestMapping("/api")
public class MovementResource {

    private final Logger log = LoggerFactory.getLogger(MovementResource.class);

    private static final String ENTITY_NAME = "movement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovementService movementService;

    private final MovementRepository movementRepository;

    private final MovementQueryService movementQueryService;

    public MovementResource(
        MovementService movementService,
        MovementRepository movementRepository,
        MovementQueryService movementQueryService
    ) {
        this.movementService = movementService;
        this.movementRepository = movementRepository;
        this.movementQueryService = movementQueryService;
    }

    /**
     * {@code POST  /movements} : Create a new movement.
     *
     * @param movementDTO the movementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movementDTO, or with status {@code 400 (Bad Request)} if the movement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movements")
    public ResponseEntity<MovementDTO> createMovement(@Valid @RequestBody MovementDTO movementDTO) throws URISyntaxException {
        log.debug("REST request to save Movement : {}", movementDTO);
        if (movementDTO.getId() != null) {
            throw new BadRequestAlertException("A new movement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovementDTO result = movementService.save(movementDTO);
        return ResponseEntity
            .created(new URI("/api/movements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movements/:id} : Updates an existing movement.
     *
     * @param id          the id of the movementDTO to save.
     * @param movementDTO the movementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementDTO,
     * or with status {@code 400 (Bad Request)} if the movementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movements/{id}")
    public ResponseEntity<MovementDTO> updateMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MovementDTO movementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Movement : {}, {}", id, movementDTO);
        if (movementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovementDTO result = movementService.update(movementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movements/:id} : Partial updates given fields of an existing movement, field will ignore if it is null
     *
     * @param id          the id of the movementDTO to save.
     * @param movementDTO the movementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementDTO,
     * or with status {@code 400 (Bad Request)} if the movementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovementDTO> partialUpdateMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MovementDTO movementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Movement partially : {}, {}", id, movementDTO);
        if (movementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovementDTO> result = movementService.partialUpdate(movementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /movements} : get all the movements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movements in body.
     */
    @GetMapping("/movements")
    public ResponseEntity<List<MovementDTO>> getAllMovements(
        MovementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Movements by criteria: {}", criteria);
        Page<MovementDTO> page = movementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movements/count} : count all the movements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/movements/count")
    public ResponseEntity<Long> countMovements(MovementCriteria criteria) {
        log.debug("REST request to count Movements by criteria: {}", criteria);
        return ResponseEntity.ok().body(movementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /movements/:id} : get the "id" movement.
     *
     * @param id the id of the movementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movements/{id}")
    public ResponseEntity<MovementDTO> getMovement(@PathVariable Long id) {
        log.debug("REST request to get Movement : {}", id);
        Optional<MovementDTO> movementDTO = movementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movementDTO);
    }

    /**
     * {@code DELETE  /movements/:id} : delete the "id" movement.
     *
     * @param id the id of the movementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movements/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        log.debug("REST request to delete Movement : {}", id);
        movementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /movements/delete-details/:id} : delete the "id" movement.
     *
     * @param id the id of the movement to delete .
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/movements/delete-details/{id}")
    public ResponseEntity<Void> deleteMovementDetails(@PathVariable Long id) {
        log.debug("REST request to delete all details of a Movement by its id : {}", id);
        movementService.deleteDetails(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/movements/confirm/{id}")
    public ResponseEntity<MovementDTO> confirmMovement(@PathVariable(value = "id") final Long id) {
        log.debug("REST request to confirm Movement by id : {}", id);

        if (!movementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        MovementDTO result;
        try {
            result = movementService.confirmMovement(id);
        } catch (Exception e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "errorMovement");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
