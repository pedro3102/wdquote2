package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.SystemaRepository;
import com.sapwd.wdquote.service.SystemaService;
import com.sapwd.wdquote.service.dto.SystemaDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.Systema}.
 */
@RestController
@RequestMapping("/api")
public class SystemaResource {

    private final Logger log = LoggerFactory.getLogger(SystemaResource.class);

    private static final String ENTITY_NAME = "system";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemaService systemService;

    private final SystemaRepository systemRepository;

    public SystemaResource(SystemaService systemService, SystemaRepository systemRepository) {
        this.systemService = systemService;
        this.systemRepository = systemRepository;
    }

    /**
     * {@code POST  /systems} : Create a new system.
     *
     * @param systemDTO the systemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemDTO, or with status {@code 400 (Bad Request)} if the system has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/systems")
    public ResponseEntity<SystemaDTO> createSystem(@Valid @RequestBody SystemaDTO systemDTO) throws URISyntaxException {
        log.debug("REST request to save System : {}", systemDTO);
        if (systemDTO.getId() != null) {
            throw new BadRequestAlertException("A new system cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemaDTO result = systemService.save(systemDTO);
        return ResponseEntity
            .created(new URI("/api/systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /systems/:id} : Updates an existing system.
     *
     * @param id the id of the systemDTO to save.
     * @param systemDTO the systemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemDTO,
     * or with status {@code 400 (Bad Request)} if the systemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/systems/{id}")
    public ResponseEntity<SystemaDTO> updateSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemaDTO systemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update System : {}, {}", id, systemDTO);
        if (systemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemaDTO result = systemService.update(systemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /systems/:id} : Partial updates given fields of an existing system, field will ignore if it is null
     *
     * @param id the id of the systemDTO to save.
     * @param systemDTO the systemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemDTO,
     * or with status {@code 400 (Bad Request)} if the systemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/systems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemaDTO> partialUpdateSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemaDTO systemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update System partially : {}, {}", id, systemDTO);
        if (systemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemaDTO> result = systemService.partialUpdate(systemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /systems} : get all the systems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systems in body.
     */
    @GetMapping("/systems")
    public ResponseEntity<List<SystemaDTO>> getAllSystems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Systems");
        Page<SystemaDTO> page = systemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /systems/:id} : get the "id" system.
     *
     * @param id the id of the systemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/systems/{id}")
    public ResponseEntity<SystemaDTO> getSystem(@PathVariable Long id) {
        log.debug("REST request to get System : {}", id);
        Optional<SystemaDTO> systemDTO = systemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemDTO);
    }

    /**
     * {@code DELETE  /systems/:id} : delete the "id" system.
     *
     * @param id the id of the systemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/systems/{id}")
    public ResponseEntity<Void> deleteSystem(@PathVariable Long id) {
        log.debug("REST request to delete System : {}", id);
        systemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /systems/basic} : get all the systems with basic info.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systems in body.
     */
    @GetMapping("/systems/basic")
    public ResponseEntity<List<SystemaDTO>> getAllSystemsBasic(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Systems");
        Page<SystemaDTO> page = systemService.findAllBasic(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
