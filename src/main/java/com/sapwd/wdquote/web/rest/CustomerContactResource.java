package com.sapwd.wdquote.web.rest;

import com.sapwd.wdquote.repository.CustomerContactRepository;
import com.sapwd.wdquote.service.CustomerContactQueryService;
import com.sapwd.wdquote.service.CustomerContactService;
import com.sapwd.wdquote.service.criteria.CustomerContactCriteria;
import com.sapwd.wdquote.service.dto.CustomerContactDTO;
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
 * REST controller for managing {@link com.sapwd.wdquote.domain.CustomerContact}.
 */
@RestController
@RequestMapping("/api")
public class CustomerContactResource {

    private final Logger log = LoggerFactory.getLogger(CustomerContactResource.class);

    private static final String ENTITY_NAME = "customerContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerContactService customerContactService;

    private final CustomerContactRepository customerContactRepository;

    private final CustomerContactQueryService customerContactQueryService;

    public CustomerContactResource(
        CustomerContactService customerContactService,
        CustomerContactRepository customerContactRepository,
        CustomerContactQueryService customerContactQueryService
    ) {
        this.customerContactService = customerContactService;
        this.customerContactRepository = customerContactRepository;
        this.customerContactQueryService = customerContactQueryService;
    }

    /**
     * {@code POST  /customer-contacts} : Create a new customerContact.
     *
     * @param customerContactDTO the customerContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerContactDTO, or with status {@code 400 (Bad Request)} if the customerContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-contacts")
    public ResponseEntity<CustomerContactDTO> createCustomerContact(@Valid @RequestBody CustomerContactDTO customerContactDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerContact : {}", customerContactDTO);
        if (customerContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerContactDTO result = customerContactService.save(customerContactDTO);
        return ResponseEntity
            .created(new URI("/api/customer-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-contacts/:id} : Updates an existing customerContact.
     *
     * @param id the id of the customerContactDTO to save.
     * @param customerContactDTO the customerContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerContactDTO,
     * or with status {@code 400 (Bad Request)} if the customerContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-contacts/{id}")
    public ResponseEntity<CustomerContactDTO> updateCustomerContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerContactDTO customerContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerContact : {}, {}", id, customerContactDTO);
        if (customerContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerContactDTO result = customerContactService.update(customerContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerContactDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-contacts/:id} : Partial updates given fields of an existing customerContact, field will ignore if it is null
     *
     * @param id the id of the customerContactDTO to save.
     * @param customerContactDTO the customerContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerContactDTO,
     * or with status {@code 400 (Bad Request)} if the customerContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerContactDTO> partialUpdateCustomerContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerContactDTO customerContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerContact partially : {}, {}", id, customerContactDTO);
        if (customerContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerContactDTO> result = customerContactService.partialUpdate(customerContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-contacts} : get all the customerContacts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerContacts in body.
     */
    @GetMapping("/customer-contacts")
    public ResponseEntity<List<CustomerContactDTO>> getAllCustomerContacts(
        CustomerContactCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerContacts by criteria: {}", criteria);
        Page<CustomerContactDTO> page = customerContactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-contacts/count} : count all the customerContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-contacts/count")
    public ResponseEntity<Long> countCustomerContacts(CustomerContactCriteria criteria) {
        log.debug("REST request to count CustomerContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerContactQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-contacts/:id} : get the "id" customerContact.
     *
     * @param id the id of the customerContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-contacts/{id}")
    public ResponseEntity<CustomerContactDTO> getCustomerContact(@PathVariable Long id) {
        log.debug("REST request to get CustomerContact : {}", id);
        Optional<CustomerContactDTO> customerContactDTO = customerContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerContactDTO);
    }

    /**
     * {@code DELETE  /customer-contacts/:id} : delete the "id" customerContact.
     *
     * @param id the id of the customerContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-contacts/{id}")
    public ResponseEntity<Void> deleteCustomerContact(@PathVariable Long id) {
        log.debug("REST request to delete CustomerContact : {}", id);
        customerContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
