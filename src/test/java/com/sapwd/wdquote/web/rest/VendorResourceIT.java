package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Vendor;
import com.sapwd.wdquote.repository.VendorRepository;
import com.sapwd.wdquote.service.dto.VendorDTO;
import com.sapwd.wdquote.service.mapper.VendorMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VendorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendorResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendorMockMvc;

    private Vendor vendor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createEntity(EntityManager em) {
        Vendor vendor = new Vendor().code(DEFAULT_CODE).name(DEFAULT_NAME).address(DEFAULT_ADDRESS);
        return vendor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createUpdatedEntity(EntityManager em) {
        Vendor vendor = new Vendor().code(UPDATED_CODE).name(UPDATED_NAME).address(UPDATED_ADDRESS);
        return vendor;
    }

    @BeforeEach
    public void initTest() {
        vendor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendor() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();
        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);
        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isCreated());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVendor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVendor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createVendorWithExistingId() throws Exception {
        // Create the Vendor with an existing ID
        vendor.setId(1L);
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setCode(null);

        // Create the Vendor, which fails.
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setName(null);

        // Create the Vendor, which fails.
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendors() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        restVendorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendor.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getVendorsByIdFiltering() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        Long id = vendor.getId();

        defaultVendorShouldBeFound("id.equals=" + id);
        defaultVendorShouldNotBeFound("id.notEquals=" + id);

        defaultVendorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVendorShouldNotBeFound("id.greaterThan=" + id);

        defaultVendorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVendorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVendorsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where code equals to DEFAULT_CODE
        defaultVendorShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the vendorList where code equals to UPDATED_CODE
        defaultVendorShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVendorsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where code in DEFAULT_CODE or UPDATED_CODE
        defaultVendorShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the vendorList where code equals to UPDATED_CODE
        defaultVendorShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVendorsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where code is not null
        defaultVendorShouldBeFound("code.specified=true");

        // Get all the vendorList where code is null
        defaultVendorShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllVendorsByCodeContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where code contains DEFAULT_CODE
        defaultVendorShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the vendorList where code contains UPDATED_CODE
        defaultVendorShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVendorsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where code does not contain DEFAULT_CODE
        defaultVendorShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the vendorList where code does not contain UPDATED_CODE
        defaultVendorShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVendorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where name equals to DEFAULT_NAME
        defaultVendorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vendorList where name equals to UPDATED_NAME
        defaultVendorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVendorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVendorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vendorList where name equals to UPDATED_NAME
        defaultVendorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVendorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where name is not null
        defaultVendorShouldBeFound("name.specified=true");

        // Get all the vendorList where name is null
        defaultVendorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVendorsByNameContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where name contains DEFAULT_NAME
        defaultVendorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vendorList where name contains UPDATED_NAME
        defaultVendorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVendorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where name does not contain DEFAULT_NAME
        defaultVendorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vendorList where name does not contain UPDATED_NAME
        defaultVendorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVendorsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where address equals to DEFAULT_ADDRESS
        defaultVendorShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the vendorList where address equals to UPDATED_ADDRESS
        defaultVendorShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllVendorsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultVendorShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the vendorList where address equals to UPDATED_ADDRESS
        defaultVendorShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllVendorsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where address is not null
        defaultVendorShouldBeFound("address.specified=true");

        // Get all the vendorList where address is null
        defaultVendorShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllVendorsByAddressContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where address contains DEFAULT_ADDRESS
        defaultVendorShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the vendorList where address contains UPDATED_ADDRESS
        defaultVendorShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllVendorsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where address does not contain DEFAULT_ADDRESS
        defaultVendorShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the vendorList where address does not contain UPDATED_ADDRESS
        defaultVendorShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVendorShouldBeFound(String filter) throws Exception {
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVendorShouldNotBeFound(String filter) throws Exception {
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVendor() throws Exception {
        // Get the vendor
        restVendorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor
        Vendor updatedVendor = vendorRepository.findById(vendor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVendor are not directly saved in db
        em.detach(updatedVendor);
        updatedVendor.code(UPDATED_CODE).name(UPDATED_NAME).address(UPDATED_ADDRESS);
        VendorDTO vendorDTO = vendorMapper.toDto(updatedVendor);

        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVendor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVendor.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        partialUpdatedVendor.code(UPDATED_CODE).address(UPDATED_ADDRESS);

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVendor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVendor.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        partialUpdatedVendor.code(UPDATED_CODE).name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVendor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVendor.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeDelete = vendorRepository.findAll().size();

        // Delete the vendor
        restVendorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
