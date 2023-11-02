package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.StockPositionRepository;
import com.sapwd.wdquote.service.StockPositionService;
import com.sapwd.wdquote.service.dto.StockPositionDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.StockPosition}.
 */
@RestController
@RequestMapping("/api")
public class StockPositionResource {

    private static final String ENTITY_NAME = "stockPosition";
    private final Logger log = LoggerFactory.getLogger(StockPositionResource.class);
    private final StockPositionService stockPositionService;
    private final StockPositionRepository stockPositionRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public StockPositionResource(StockPositionService stockPositionService, StockPositionRepository stockPositionRepository) {
        this.stockPositionService = stockPositionService;
        this.stockPositionRepository = stockPositionRepository;
    }

    /**
     * {@code POST  /stock-positions} : Create a new stockPosition.
     *
     * @param stockPositionDTO the stockPositionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockPositionDTO, or with status {@code 400 (Bad Request)} if the stockPosition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stock-positions")
    public ResponseEntity<StockPositionDTO> createStockPosition(@Valid @RequestBody StockPositionDTO stockPositionDTO)
        throws URISyntaxException {
        log.debug("REST request to save StockPosition : {}", stockPositionDTO);
        if (stockPositionDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockPosition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockPositionDTO result = stockPositionService.save(stockPositionDTO);
        return ResponseEntity
            .created(new URI("/api/stock-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-positions/:id} : Updates an existing stockPosition.
     *
     * @param id               the id of the stockPositionDTO to save.
     * @param stockPositionDTO the stockPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockPositionDTO,
     * or with status {@code 400 (Bad Request)} if the stockPositionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stock-positions/{id}")
    public ResponseEntity<StockPositionDTO> updateStockPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockPositionDTO stockPositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StockPosition : {}, {}", id, stockPositionDTO);
        if (stockPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockPositionDTO result = stockPositionService.update(stockPositionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stock-positions/:id} : Partial updates given fields of an existing stockPosition, field will ignore if it is null
     *
     * @param id               the id of the stockPositionDTO to save.
     * @param stockPositionDTO the stockPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockPositionDTO,
     * or with status {@code 400 (Bad Request)} if the stockPositionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockPositionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stock-positions/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<StockPositionDTO> partialUpdateStockPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockPositionDTO stockPositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockPosition partially : {}, {}", id, stockPositionDTO);
        if (stockPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockPositionDTO> result = stockPositionService.partialUpdate(stockPositionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockPositionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-positions} : get all the stockPositions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockPositions in body.
     */
    @GetMapping("/stock-positions")
    public ResponseEntity<List<StockPositionDTO>> getAllStockPositions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StockPositions");
        Page<StockPositionDTO> page = stockPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-positions/:id} : get the "id" stockPosition.
     *
     * @param id the id of the stockPositionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockPositionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stock-positions/{id}")
    public ResponseEntity<StockPositionDTO> getStockPosition(@PathVariable Long id) {
        log.debug("REST request to get StockPosition : {}", id);
        Optional<StockPositionDTO> stockPositionDTO = stockPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockPositionDTO);
    }

    /**
     * {@code DELETE  /stock-positions/:id} : delete the "id" stockPosition.
     *
     * @param id the id of the stockPositionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stock-positions/{id}")
    public ResponseEntity<Void> deleteStockPosition(@PathVariable Long id) {
        log.debug("REST request to delete StockPosition : {}", id);
        stockPositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /stock-positions} : get all the stockPositions Basic.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockPositions in body.
     */
    @GetMapping("/stock-positions/basic")
    public ResponseEntity<List<StockPositionDTO>> getAllStockPositionsBasic() {
        log.debug("REST request to get a page of StockPositions Basic");
        List<StockPositionDTO> stockPositionDTOList = stockPositionService.findAllBasic();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "List of Stock Positions Basic", ENTITY_NAME))
            .body(stockPositionDTOList);
    }
}
