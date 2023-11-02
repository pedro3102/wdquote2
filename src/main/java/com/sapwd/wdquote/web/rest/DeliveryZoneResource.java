package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.DeliveryZoneRepository;
import com.sapwd.wdquote.service.DeliveryZoneService;
import com.sapwd.wdquote.service.dto.DeliveryZoneDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.DeliveryZone}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryZoneResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryZoneResource.class);

    private static final String ENTITY_NAME = "deliveryZone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryZoneService deliveryZoneService;

    private final DeliveryZoneRepository deliveryZoneRepository;

    public DeliveryZoneResource(DeliveryZoneService deliveryZoneService, DeliveryZoneRepository deliveryZoneRepository) {
        this.deliveryZoneService = deliveryZoneService;
        this.deliveryZoneRepository = deliveryZoneRepository;
    }

    /**
     * {@code POST  /delivery-zones} : Create a new deliveryZone.
     *
     * @param deliveryZoneDTO the deliveryZoneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryZoneDTO, or with status {@code 400 (Bad Request)} if the deliveryZone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-zones")
    public ResponseEntity<DeliveryZoneDTO> createDeliveryZone(@Valid @RequestBody DeliveryZoneDTO deliveryZoneDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryZone : {}", deliveryZoneDTO);
        if (deliveryZoneDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryZone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryZoneDTO result = deliveryZoneService.save(deliveryZoneDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-zones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-zones/:id} : Updates an existing deliveryZone.
     *
     * @param id the id of the deliveryZoneDTO to save.
     * @param deliveryZoneDTO the deliveryZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryZoneDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryZoneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-zones/{id}")
    public ResponseEntity<DeliveryZoneDTO> updateDeliveryZone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryZoneDTO deliveryZoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryZone : {}, {}", id, deliveryZoneDTO);
        if (deliveryZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryZoneDTO result = deliveryZoneService.update(deliveryZoneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryZoneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-zones/:id} : Partial updates given fields of an existing deliveryZone, field will ignore if it is null
     *
     * @param id the id of the deliveryZoneDTO to save.
     * @param deliveryZoneDTO the deliveryZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryZoneDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryZoneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryZoneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-zones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryZoneDTO> partialUpdateDeliveryZone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryZoneDTO deliveryZoneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryZone partially : {}, {}", id, deliveryZoneDTO);
        if (deliveryZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryZoneDTO> result = deliveryZoneService.partialUpdate(deliveryZoneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryZoneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-zones} : get all the deliveryZones.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryZones in body.
     */
    @GetMapping("/delivery-zones")
    public ResponseEntity<List<DeliveryZoneDTO>> getAllDeliveryZones(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DeliveryZones");
        Page<DeliveryZoneDTO> page = deliveryZoneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-zones/:id} : get the "id" deliveryZone.
     *
     * @param id the id of the deliveryZoneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryZoneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-zones/{id}")
    public ResponseEntity<DeliveryZoneDTO> getDeliveryZone(@PathVariable Long id) {
        log.debug("REST request to get DeliveryZone : {}", id);
        Optional<DeliveryZoneDTO> deliveryZoneDTO = deliveryZoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryZoneDTO);
    }

    /**
     * {@code DELETE  /delivery-zones/:id} : delete the "id" deliveryZone.
     *
     * @param id the id of the deliveryZoneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-zones/{id}")
    public ResponseEntity<Void> deleteDeliveryZone(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryZone : {}", id);
        deliveryZoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
