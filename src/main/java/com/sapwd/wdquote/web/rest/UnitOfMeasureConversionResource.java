package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.UnitOfMeasureConversionRepository;
import com.sapwd.wdquote.service.UnitOfMeasureConversionQueryService;
import com.sapwd.wdquote.service.UnitOfMeasureConversionService;
import com.sapwd.wdquote.service.criteria.UnitOfMeasureConversionCriteria;
import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.UnitOfMeasureConversion}.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureConversionResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureConversionResource.class);

    private static final String ENTITY_NAME = "unitOfMeasureConversion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasureConversionService unitOfMeasureConversionService;
    private final UnitOfMeasureConversionQueryService unitOfMeasureConversionQueryService;

    private final UnitOfMeasureConversionRepository unitOfMeasureConversionRepository;

    public UnitOfMeasureConversionResource(
        UnitOfMeasureConversionService unitOfMeasureConversionService,
        UnitOfMeasureConversionQueryService unitOfMeasureConversionQueryService,
        UnitOfMeasureConversionRepository unitOfMeasureConversionRepository
    ) {
        this.unitOfMeasureConversionService = unitOfMeasureConversionService;
        this.unitOfMeasureConversionQueryService = unitOfMeasureConversionQueryService;
        this.unitOfMeasureConversionRepository = unitOfMeasureConversionRepository;
    }

    /**
     * {@code POST  /unit-of-measure-conversions} : Create a new unitOfMeasureConversion.
     *
     * @param unitOfMeasureConversionDTO the unitOfMeasureConversionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasureConversionDTO, or with status {@code 400 (Bad Request)} if the unitOfMeasureConversion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measure-conversions")
    public ResponseEntity<UnitOfMeasureConversionDTO> createUnitOfMeasureConversion(
        @Valid @RequestBody UnitOfMeasureConversionDTO unitOfMeasureConversionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasureConversion : {}", unitOfMeasureConversionDTO);
        if (unitOfMeasureConversionDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasureConversion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitOfMeasureConversionDTO result = unitOfMeasureConversionService.save(unitOfMeasureConversionDTO);
        return ResponseEntity
            .created(new URI("/api/unit-of-measure-conversions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measure-conversions/:id} : Updates an existing unitOfMeasureConversion.
     *
     * @param id the id of the unitOfMeasureConversionDTO to save.
     * @param unitOfMeasureConversionDTO the unitOfMeasureConversionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasureConversionDTO,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasureConversionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasureConversionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measure-conversions/{id}")
    public ResponseEntity<UnitOfMeasureConversionDTO> updateUnitOfMeasureConversion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitOfMeasureConversionDTO unitOfMeasureConversionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasureConversion : {}, {}", id, unitOfMeasureConversionDTO);
        if (unitOfMeasureConversionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasureConversionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureConversionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitOfMeasureConversionDTO result = unitOfMeasureConversionService.update(unitOfMeasureConversionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitOfMeasureConversionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-of-measure-conversions/:id} : Partial updates given fields of an existing unitOfMeasureConversion, field will ignore if it is null
     *
     * @param id the id of the unitOfMeasureConversionDTO to save.
     * @param unitOfMeasureConversionDTO the unitOfMeasureConversionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasureConversionDTO,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasureConversionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitOfMeasureConversionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasureConversionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-of-measure-conversions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitOfMeasureConversionDTO> partialUpdateUnitOfMeasureConversion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitOfMeasureConversionDTO unitOfMeasureConversionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitOfMeasureConversion partially : {}, {}", id, unitOfMeasureConversionDTO);
        if (unitOfMeasureConversionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasureConversionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureConversionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitOfMeasureConversionDTO> result = unitOfMeasureConversionService.partialUpdate(unitOfMeasureConversionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitOfMeasureConversionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-of-measure-conversions} : get all the unitOfMeasureConversions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasureConversions in body.
     */
    @GetMapping("/unit-of-measure-conversions")
    public ResponseEntity<List<UnitOfMeasureConversionDTO>> getAllUnitOfMeasureConversions(
        UnitOfMeasureConversionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UnitOfMeasureConversions");
        Page<UnitOfMeasureConversionDTO> page = unitOfMeasureConversionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-of-measure-conversions/:id} : get the "id" unitOfMeasureConversion.
     *
     * @param id the id of the unitOfMeasureConversionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasureConversionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measure-conversions/{id}")
    public ResponseEntity<UnitOfMeasureConversionDTO> getUnitOfMeasureConversion(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasureConversion : {}", id);
        Optional<UnitOfMeasureConversionDTO> unitOfMeasureConversionDTO = unitOfMeasureConversionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasureConversionDTO);
    }

    /**
     * {@code DELETE  /unit-of-measure-conversions/:id} : delete the "id" unitOfMeasureConversion.
     *
     * @param id the id of the unitOfMeasureConversionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measure-conversions/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasureConversion(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasureConversion : {}", id);
        unitOfMeasureConversionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
