package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.TaxAreaCode;
import com.sapwd.wdquote.repository.TaxAreaCodeRepository;
import com.sapwd.wdquote.service.dto.TaxAreaCodeDTO;
import com.sapwd.wdquote.service.mapper.TaxAreaCodeMapper;
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
 * Integration tests for the {@link TaxAreaCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxAreaCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tax-area-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxAreaCodeRepository taxAreaCodeRepository;

    @Autowired
    private TaxAreaCodeMapper taxAreaCodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxAreaCodeMockMvc;

    private TaxAreaCode taxAreaCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAreaCode createEntity(EntityManager em) {
        TaxAreaCode taxAreaCode = new TaxAreaCode().code(DEFAULT_CODE).name(DEFAULT_NAME);
        return taxAreaCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAreaCode createUpdatedEntity(EntityManager em) {
        TaxAreaCode taxAreaCode = new TaxAreaCode().code(UPDATED_CODE).name(UPDATED_NAME);
        return taxAreaCode;
    }

    @BeforeEach
    public void initTest() {
        taxAreaCode = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxAreaCode() throws Exception {
        int databaseSizeBeforeCreate = taxAreaCodeRepository.findAll().size();
        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);
        restTaxAreaCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeCreate + 1);
        TaxAreaCode testTaxAreaCode = taxAreaCodeList.get(taxAreaCodeList.size() - 1);
        assertThat(testTaxAreaCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTaxAreaCode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTaxAreaCodeWithExistingId() throws Exception {
        // Create the TaxAreaCode with an existing ID
        taxAreaCode.setId(1L);
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        int databaseSizeBeforeCreate = taxAreaCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxAreaCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAreaCodeRepository.findAll().size();
        // set the field null
        taxAreaCode.setCode(null);

        // Create the TaxAreaCode, which fails.
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        restTaxAreaCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAreaCodeRepository.findAll().size();
        // set the field null
        taxAreaCode.setName(null);

        // Create the TaxAreaCode, which fails.
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        restTaxAreaCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxAreaCodes() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        // Get all the taxAreaCodeList
        restTaxAreaCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAreaCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTaxAreaCode() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        // Get the taxAreaCode
        restTaxAreaCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, taxAreaCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxAreaCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTaxAreaCode() throws Exception {
        // Get the taxAreaCode
        restTaxAreaCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxAreaCode() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();

        // Update the taxAreaCode
        TaxAreaCode updatedTaxAreaCode = taxAreaCodeRepository.findById(taxAreaCode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxAreaCode are not directly saved in db
        em.detach(updatedTaxAreaCode);
        updatedTaxAreaCode.code(UPDATED_CODE).name(UPDATED_NAME);
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(updatedTaxAreaCode);

        restTaxAreaCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAreaCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
        TaxAreaCode testTaxAreaCode = taxAreaCodeList.get(taxAreaCodeList.size() - 1);
        assertThat(testTaxAreaCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaxAreaCode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAreaCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxAreaCodeWithPatch() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();

        // Update the taxAreaCode using partial update
        TaxAreaCode partialUpdatedTaxAreaCode = new TaxAreaCode();
        partialUpdatedTaxAreaCode.setId(taxAreaCode.getId());

        partialUpdatedTaxAreaCode.code(UPDATED_CODE);

        restTaxAreaCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAreaCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxAreaCode))
            )
            .andExpect(status().isOk());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
        TaxAreaCode testTaxAreaCode = taxAreaCodeList.get(taxAreaCodeList.size() - 1);
        assertThat(testTaxAreaCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaxAreaCode.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTaxAreaCodeWithPatch() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();

        // Update the taxAreaCode using partial update
        TaxAreaCode partialUpdatedTaxAreaCode = new TaxAreaCode();
        partialUpdatedTaxAreaCode.setId(taxAreaCode.getId());

        partialUpdatedTaxAreaCode.code(UPDATED_CODE).name(UPDATED_NAME);

        restTaxAreaCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAreaCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxAreaCode))
            )
            .andExpect(status().isOk());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
        TaxAreaCode testTaxAreaCode = taxAreaCodeList.get(taxAreaCodeList.size() - 1);
        assertThat(testTaxAreaCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTaxAreaCode.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxAreaCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxAreaCode() throws Exception {
        int databaseSizeBeforeUpdate = taxAreaCodeRepository.findAll().size();
        taxAreaCode.setId(count.incrementAndGet());

        // Create the TaxAreaCode
        TaxAreaCodeDTO taxAreaCodeDTO = taxAreaCodeMapper.toDto(taxAreaCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAreaCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxAreaCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAreaCode in the database
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxAreaCode() throws Exception {
        // Initialize the database
        taxAreaCodeRepository.saveAndFlush(taxAreaCode);

        int databaseSizeBeforeDelete = taxAreaCodeRepository.findAll().size();

        // Delete the taxAreaCode
        restTaxAreaCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxAreaCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxAreaCode> taxAreaCodeList = taxAreaCodeRepository.findAll();
        assertThat(taxAreaCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
