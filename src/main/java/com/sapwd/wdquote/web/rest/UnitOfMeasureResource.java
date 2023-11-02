package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.UnitOfMeasureRepository;
import com.sapwd.wdquote.service.UnitOfMeasureService;
import com.sapwd.wdquote.service.dto.UnitOfMeasureDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.UnitOfMeasure}.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private static final String ENTITY_NAME = "unitOfMeasure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasureService unitOfMeasureService;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureResource(UnitOfMeasureService unitOfMeasureService, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureService = unitOfMeasureService;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    /**
     * {@code POST  /unit-of-measures} : Create a new unitOfMeasure.
     *
     * @param unitOfMeasureDTO the unitOfMeasureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasureDTO, or with status {@code 400 (Bad Request)} if the unitOfMeasure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasureDTO> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO)
        throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasure : {}", unitOfMeasureDTO);
        if (unitOfMeasureDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitOfMeasureDTO result = unitOfMeasureService.save(unitOfMeasureDTO);
        return ResponseEntity
            .created(new URI("/api/unit-of-measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measures/:id} : Updates an existing unitOfMeasure.
     *
     * @param id the id of the unitOfMeasureDTO to save.
     * @param unitOfMeasureDTO the unitOfMeasureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasureDTO,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasureDTO> updateUnitOfMeasure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasure : {}, {}", id, unitOfMeasureDTO);
        if (unitOfMeasureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitOfMeasureDTO result = unitOfMeasureService.update(unitOfMeasureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitOfMeasureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-of-measures/:id} : Partial updates given fields of an existing unitOfMeasure, field will ignore if it is null
     *
     * @param id the id of the unitOfMeasureDTO to save.
     * @param unitOfMeasureDTO the unitOfMeasureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasureDTO,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitOfMeasureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-of-measures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitOfMeasureDTO> partialUpdateUnitOfMeasure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitOfMeasureDTO unitOfMeasureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitOfMeasure partially : {}, {}", id, unitOfMeasureDTO);
        if (unitOfMeasureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitOfMeasureDTO> result = unitOfMeasureService.partialUpdate(unitOfMeasureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitOfMeasureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-of-measures} : get all the unitOfMeasures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasures in body.
     */
    @GetMapping("/unit-of-measures")
    public ResponseEntity<List<UnitOfMeasureDTO>> getAllUnitOfMeasures(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UnitOfMeasures");
        Page<UnitOfMeasureDTO> page = unitOfMeasureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-of-measures/:id} : get the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasureDTO> getUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasure : {}", id);
        Optional<UnitOfMeasureDTO> unitOfMeasureDTO = unitOfMeasureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasureDTO);
    }

    /**
     * {@code DELETE  /unit-of-measures/:id} : delete the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measures/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasure : {}", id);
        unitOfMeasureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
