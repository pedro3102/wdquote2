package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.TaxAreaCodeRepository;
import com.sapwd.wdquote.service.TaxAreaCodeService;
import com.sapwd.wdquote.service.dto.TaxAreaCodeDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.TaxAreaCode}.
 */
@RestController
@RequestMapping("/api")
public class TaxAreaCodeResource {

    private final Logger log = LoggerFactory.getLogger(TaxAreaCodeResource.class);

    private static final String ENTITY_NAME = "taxAreaCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxAreaCodeService taxAreaCodeService;

    private final TaxAreaCodeRepository taxAreaCodeRepository;

    public TaxAreaCodeResource(TaxAreaCodeService taxAreaCodeService, TaxAreaCodeRepository taxAreaCodeRepository) {
        this.taxAreaCodeService = taxAreaCodeService;
        this.taxAreaCodeRepository = taxAreaCodeRepository;
    }

    /**
     * {@code POST  /tax-area-codes} : Create a new taxAreaCode.
     *
     * @param taxAreaCodeDTO the taxAreaCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxAreaCodeDTO, or with status {@code 400 (Bad Request)} if the taxAreaCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-area-codes")
    public ResponseEntity<TaxAreaCodeDTO> createTaxAreaCode(@Valid @RequestBody TaxAreaCodeDTO taxAreaCodeDTO) throws URISyntaxException {
        log.debug("REST request to save TaxAreaCode : {}", taxAreaCodeDTO);
        if (taxAreaCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxAreaCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxAreaCodeDTO result = taxAreaCodeService.save(taxAreaCodeDTO);
        return ResponseEntity
            .created(new URI("/api/tax-area-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-area-codes/:id} : Updates an existing taxAreaCode.
     *
     * @param id the id of the taxAreaCodeDTO to save.
     * @param taxAreaCodeDTO the taxAreaCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAreaCodeDTO,
     * or with status {@code 400 (Bad Request)} if the taxAreaCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxAreaCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-area-codes/{id}")
    public ResponseEntity<TaxAreaCodeDTO> updateTaxAreaCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxAreaCodeDTO taxAreaCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxAreaCode : {}, {}", id, taxAreaCodeDTO);
        if (taxAreaCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAreaCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAreaCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxAreaCodeDTO result = taxAreaCodeService.update(taxAreaCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAreaCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tax-area-codes/:id} : Partial updates given fields of an existing taxAreaCode, field will ignore if it is null
     *
     * @param id the id of the taxAreaCodeDTO to save.
     * @param taxAreaCodeDTO the taxAreaCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAreaCodeDTO,
     * or with status {@code 400 (Bad Request)} if the taxAreaCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxAreaCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxAreaCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-area-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxAreaCodeDTO> partialUpdateTaxAreaCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxAreaCodeDTO taxAreaCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxAreaCode partially : {}, {}", id, taxAreaCodeDTO);
        if (taxAreaCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxAreaCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxAreaCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxAreaCodeDTO> result = taxAreaCodeService.partialUpdate(taxAreaCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAreaCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-area-codes} : get all the taxAreaCodes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxAreaCodes in body.
     */
    @GetMapping("/tax-area-codes")
    public ResponseEntity<List<TaxAreaCodeDTO>> getAllTaxAreaCodes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TaxAreaCodes");
        Page<TaxAreaCodeDTO> page = taxAreaCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-area-codes/:id} : get the "id" taxAreaCode.
     *
     * @param id the id of the taxAreaCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxAreaCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-area-codes/{id}")
    public ResponseEntity<TaxAreaCodeDTO> getTaxAreaCode(@PathVariable Long id) {
        log.debug("REST request to get TaxAreaCode : {}", id);
        Optional<TaxAreaCodeDTO> taxAreaCodeDTO = taxAreaCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxAreaCodeDTO);
    }

    /**
     * {@code DELETE  /tax-area-codes/:id} : delete the "id" taxAreaCode.
     *
     * @param id the id of the taxAreaCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-area-codes/{id}")
    public ResponseEntity<Void> deleteTaxAreaCode(@PathVariable Long id) {
        log.debug("REST request to delete TaxAreaCode : {}", id);
        taxAreaCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
