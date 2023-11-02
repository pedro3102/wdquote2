package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.UnitOfMeasure;
import com.sapwd.wdquote.repository.UnitOfMeasureRepository;
import com.sapwd.wdquote.service.dto.UnitOfMeasureDTO;
import com.sapwd.wdquote.service.mapper.UnitOfMeasureMapper;
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
 * Integration tests for the {@link UnitOfMeasureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitOfMeasureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ALLOWS_DECIMAL = false;
    private static final Boolean UPDATED_ALLOWS_DECIMAL = true;

    private static final String ENTITY_API_URL = "/api/unit-of-measures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private UnitOfMeasureMapper unitOfMeasureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitOfMeasureMockMvc;

    private UnitOfMeasure unitOfMeasure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .name(DEFAULT_NAME)
            .abbreviation(DEFAULT_ABBREVIATION)
            .allowsDecimal(DEFAULT_ALLOWS_DECIMAL);
        return unitOfMeasure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createUpdatedEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .name(UPDATED_NAME)
            .abbreviation(UPDATED_ABBREVIATION)
            .allowsDecimal(UPDATED_ALLOWS_DECIMAL);
        return unitOfMeasure;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasure = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitOfMeasure() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();
        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);
        restUnitOfMeasureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitOfMeasure.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
        assertThat(testUnitOfMeasure.getAllowsDecimal()).isEqualTo(DEFAULT_ALLOWS_DECIMAL);
    }

    @Test
    @Transactional
    void createUnitOfMeasureWithExistingId() throws Exception {
        // Create the UnitOfMeasure with an existing ID
        unitOfMeasure.setId(1L);
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setName(null);

        // Create the UnitOfMeasure, which fails.
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        restUnitOfMeasureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAbbreviationIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setAbbreviation(null);

        // Create the UnitOfMeasure, which fails.
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        restUnitOfMeasureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowsDecimalIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setAllowsDecimal(null);

        // Create the UnitOfMeasure, which fails.
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        restUnitOfMeasureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasures() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
            .andExpect(jsonPath("$.[*].allowsDecimal").value(hasItem(DEFAULT_ALLOWS_DECIMAL.booleanValue())));
    }

    @Test
    @Transactional
    void getUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL_ID, unitOfMeasure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION))
            .andExpect(jsonPath("$.allowsDecimal").value(DEFAULT_ALLOWS_DECIMAL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUnitOfMeasure() throws Exception {
        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure
        UnitOfMeasure updatedUnitOfMeasure = unitOfMeasureRepository.findById(unitOfMeasure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnitOfMeasure are not directly saved in db
        em.detach(updatedUnitOfMeasure);
        updatedUnitOfMeasure.name(UPDATED_NAME).abbreviation(UPDATED_ABBREVIATION).allowsDecimal(UPDATED_ALLOWS_DECIMAL);
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(updatedUnitOfMeasure);

        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitOfMeasure.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
        assertThat(testUnitOfMeasure.getAllowsDecimal()).isEqualTo(UPDATED_ALLOWS_DECIMAL);
    }

    @Test
    @Transactional
    void putNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure.name(UPDATED_NAME);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitOfMeasure.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
        assertThat(testUnitOfMeasure.getAllowsDecimal()).isEqualTo(DEFAULT_ALLOWS_DECIMAL);
    }

    @Test
    @Transactional
    void fullUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure.name(UPDATED_NAME).abbreviation(UPDATED_ABBREVIATION).allowsDecimal(UPDATED_ALLOWS_DECIMAL);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitOfMeasure.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
        assertThat(testUnitOfMeasure.getAllowsDecimal()).isEqualTo(UPDATED_ALLOWS_DECIMAL);
    }

    @Test
    @Transactional
    void patchNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitOfMeasureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // Create the UnitOfMeasure
        UnitOfMeasureDTO unitOfMeasureDTO = unitOfMeasureMapper.toDto(unitOfMeasure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeDelete = unitOfMeasureRepository.findAll().size();

        // Delete the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitOfMeasure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
