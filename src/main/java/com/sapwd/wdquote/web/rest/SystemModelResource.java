package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.SystemModelRepository;
import com.sapwd.wdquote.service.SystemModelQueryService;
import com.sapwd.wdquote.service.SystemModelService;
import com.sapwd.wdquote.service.criteria.SystemModelCriteria;
import com.sapwd.wdquote.service.dto.SystemModelDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.SystemModel}.
 */
@RestController
@RequestMapping("/api")
public class SystemModelResource {

    private final Logger log = LoggerFactory.getLogger(SystemModelResource.class);

    private static final String ENTITY_NAME = "systemModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemModelService systemModelService;

    private final SystemModelRepository systemModelRepository;

    private final SystemModelQueryService systemModelQueryService;

    public SystemModelResource(
        SystemModelService systemModelService,
        SystemModelRepository systemModelRepository,
        SystemModelQueryService systemModelQueryService
    ) {
        this.systemModelService = systemModelService;
        this.systemModelRepository = systemModelRepository;
        this.systemModelQueryService = systemModelQueryService;
    }

    /**
     * {@code POST  /system-models} : Create a new systemModel.
     *
     * @param systemModelDTO the systemModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemModelDTO, or with status {@code 400 (Bad Request)} if the systemModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-models")
    public ResponseEntity<SystemModelDTO> createSystemModel(@Valid @RequestBody SystemModelDTO systemModelDTO) throws URISyntaxException {
        log.debug("REST request to save SystemModel : {}", systemModelDTO);
        if (systemModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemModelDTO result = systemModelService.save(systemModelDTO);
        return ResponseEntity
            .created(new URI("/api/system-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-models/:id} : Updates an existing systemModel.
     *
     * @param id the id of the systemModelDTO to save.
     * @param systemModelDTO the systemModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemModelDTO,
     * or with status {@code 400 (Bad Request)} if the systemModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-models/{id}")
    public ResponseEntity<SystemModelDTO> updateSystemModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemModelDTO systemModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemModel : {}, {}", id, systemModelDTO);
        if (systemModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemModelDTO result = systemModelService.update(systemModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-models/:id} : Partial updates given fields of an existing systemModel, field will ignore if it is null
     *
     * @param id the id of the systemModelDTO to save.
     * @param systemModelDTO the systemModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemModelDTO,
     * or with status {@code 400 (Bad Request)} if the systemModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemModelDTO> partialUpdateSystemModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemModelDTO systemModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemModel partially : {}, {}", id, systemModelDTO);
        if (systemModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemModelDTO> result = systemModelService.partialUpdate(systemModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-models} : get all the systemModels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemModels in body.
     */
    @GetMapping("/system-models")
    public ResponseEntity<List<SystemModelDTO>> getAllSystemModels(
        SystemModelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SystemModels by criteria: {}", criteria);
        Page<SystemModelDTO> page = systemModelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-models/count} : count all the systemModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/system-models/count")
    public ResponseEntity<Long> countSystemModels(SystemModelCriteria criteria) {
        log.debug("REST request to count SystemModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-models/:id} : get the "id" systemModel.
     *
     * @param id the id of the systemModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-models/{id}")
    public ResponseEntity<SystemModelDTO> getSystemModel(@PathVariable Long id) {
        log.debug("REST request to get SystemModel : {}", id);
        Optional<SystemModelDTO> systemModelDTO = systemModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemModelDTO);
    }

    /**
     * {@code DELETE  /system-models/:id} : delete the "id" systemModel.
     *
     * @param id the id of the systemModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-models/{id}")
    public ResponseEntity<Void> deleteSystemModel(@PathVariable Long id) {
        log.debug("REST request to delete SystemModel : {}", id);
        systemModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /system-model/basic} : get all the system models with basic info.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of system models in body.
     */
    @GetMapping("/system-models/basic")
    public ResponseEntity<List<SystemModelDTO>> getAllSystemModelsBasic() {
        log.debug("REST request to get a list of system models with a basic info");
        List<SystemModelDTO> systemModelsDTOList = systemModelService.findAllBasic();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "List of system models ", ENTITY_NAME))
            .body(systemModelsDTOList);
    }
}
