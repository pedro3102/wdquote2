package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.repository.MovementTypeRepository;
import com.sapwd.wdquote.service.MovementTypeService;
import com.sapwd.wdquote.service.dto.MovementTypeDTO;
import com.sapwd.wdquote.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sapwd.wdquote.domain.MovementType}.
 */
@RestController
@RequestMapping("/api")
public class MovementTypeResource {

    private static final String ENTITY_NAME = "movementType";
    private final Logger log = LoggerFactory.getLogger(MovementTypeResource.class);
    private final MovementTypeService movementTypeService;
    private final MovementTypeRepository movementTypeRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MovementTypeResource(MovementTypeService movementTypeService, MovementTypeRepository movementTypeRepository) {
        this.movementTypeService = movementTypeService;
        this.movementTypeRepository = movementTypeRepository;
    }

    /**
     * {@code POST  /movement-types} : Create a new movementType.
     *
     * @param movementTypeDTO the movementTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movementTypeDTO, or with status {@code 400 (Bad Request)} if the movementType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movement-types")
    public ResponseEntity<MovementTypeDTO> createMovementType(@Valid @RequestBody MovementTypeDTO movementTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MovementType : {}", movementTypeDTO);
        if (movementTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new movementType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovementTypeDTO result = movementTypeService.save(movementTypeDTO);
        return ResponseEntity
            .created(new URI("/api/movement-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movement-types/:id} : Updates an existing movementType.
     *
     * @param id              the id of the movementTypeDTO to save.
     * @param movementTypeDTO the movementTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementTypeDTO,
     * or with status {@code 400 (Bad Request)} if the movementTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movementTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movement-types/{id}")
    public ResponseEntity<MovementTypeDTO> updateMovementType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MovementTypeDTO movementTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MovementType : {}, {}", id, movementTypeDTO);
        if (movementTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovementTypeDTO result = movementTypeService.update(movementTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movement-types/:id} : Partial updates given fields of an existing movementType, field will ignore if it is null
     *
     * @param id              the id of the movementTypeDTO to save.
     * @param movementTypeDTO the movementTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movementTypeDTO,
     * or with status {@code 400 (Bad Request)} if the movementTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movementTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movementTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movement-types/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<MovementTypeDTO> partialUpdateMovementType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MovementTypeDTO movementTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MovementType partially : {}, {}", id, movementTypeDTO);
        if (movementTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movementTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movementTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovementTypeDTO> result = movementTypeService.partialUpdate(movementTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movementTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /movement-types} : get all the movementTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movementTypes in body.
     */
    @GetMapping("/movement-types")
    public ResponseEntity<List<MovementTypeDTO>> getAllMovementTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MovementTypes");
        Page<MovementTypeDTO> page = movementTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movement-types/:id} : get the "id" movementType.
     *
     * @param id the id of the movementTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movementTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movement-types/{id}")
    public ResponseEntity<MovementTypeDTO> getMovementType(@PathVariable Long id) {
        log.debug("REST request to get MovementType : {}", id);
        Optional<MovementTypeDTO> movementTypeDTO = movementTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movementTypeDTO);
    }

    /**
     * {@code DELETE  /movement-types/:id} : delete the "id" movementType.
     *
     * @param id the id of the movementTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movement-types/{id}")
    public ResponseEntity<Void> deleteMovementType(@PathVariable Long id) {
        log.debug("REST request to delete MovementType : {}", id);
        movementTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /movement-types} : get all the movementTypes with basic info.
     *
     * @param type the OperationType type.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movementTypes in body.
     */
    @GetMapping("/movement-types/basic")
    public ResponseEntity<List<MovementTypeDTO>> getAllMovementTypesBasic(@RequestParam(required = false) OperationType type) {
        log.debug("REST request to get a list of MovementTypes by OperationType");
        List<MovementTypeDTO> movementTypeDTOList = movementTypeService.findAllBasic(type);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "List of MovementTypes by OperationType", ENTITY_NAME))
            .body(movementTypeDTOList);
    }
}
