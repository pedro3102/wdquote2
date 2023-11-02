package com.sapwd.wdquote.web.rest;

import static com.sapwd.wdquote.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.UnitOfMeasure;
import com.sapwd.wdquote.domain.UnitOfMeasureConversion;
import com.sapwd.wdquote.repository.UnitOfMeasureConversionRepository;
import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
import com.sapwd.wdquote.service.mapper.UnitOfMeasureConversionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link UnitOfMeasureConversionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitOfMeasureConversionResourceIT {

    private static final BigDecimal DEFAULT_CONVERSION_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONVERSION_FACTOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/unit-of-measure-conversions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitOfMeasureConversionRepository unitOfMeasureConversionRepository;

    @Autowired
    private UnitOfMeasureConversionMapper unitOfMeasureConversionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitOfMeasureConversionMockMvc;

    private UnitOfMeasureConversion unitOfMeasureConversion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasureConversion createEntity(EntityManager em) {
        UnitOfMeasureConversion unitOfMeasureConversion = new UnitOfMeasureConversion().conversionFactor(DEFAULT_CONVERSION_FACTOR);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        unitOfMeasureConversion.setUom(unitOfMeasure);
        // Add required entity
        unitOfMeasureConversion.setUomEquivalent(unitOfMeasure);
        return unitOfMeasureConversion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasureConversion createUpdatedEntity(EntityManager em) {
        UnitOfMeasureConversion unitOfMeasureConversion = new UnitOfMeasureConversion().conversionFactor(UPDATED_CONVERSION_FACTOR);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createUpdatedEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        unitOfMeasureConversion.setUom(unitOfMeasure);
        // Add required entity
        unitOfMeasureConversion.setUomEquivalent(unitOfMeasure);
        return unitOfMeasureConversion;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasureConversion = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureConversionRepository.findAll().size();
        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);
        restUnitOfMeasureConversionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasureConversion testUnitOfMeasureConversion = unitOfMeasureConversionList.get(unitOfMeasureConversionList.size() - 1);
        assertThat(testUnitOfMeasureConversion.getConversionFactor()).isEqualByComparingTo(DEFAULT_CONVERSION_FACTOR);
    }

    @Test
    @Transactional
    void createUnitOfMeasureConversionWithExistingId() throws Exception {
        // Create the UnitOfMeasureConversion with an existing ID
        unitOfMeasureConversion.setId(1L);
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        int databaseSizeBeforeCreate = unitOfMeasureConversionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureConversionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConversionFactorIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureConversionRepository.findAll().size();
        // set the field null
        unitOfMeasureConversion.setConversionFactor(null);

        // Create the UnitOfMeasureConversion, which fails.
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        restUnitOfMeasureConversionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasureConversions() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        // Get all the unitOfMeasureConversionList
        restUnitOfMeasureConversionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasureConversion.getId().intValue())))
            .andExpect(jsonPath("$.[*].conversionFactor").value(hasItem(sameNumber(DEFAULT_CONVERSION_FACTOR))));
    }

    @Test
    @Transactional
    void getUnitOfMeasureConversion() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        // Get the unitOfMeasureConversion
        restUnitOfMeasureConversionMockMvc
            .perform(get(ENTITY_API_URL_ID, unitOfMeasureConversion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasureConversion.getId().intValue()))
            .andExpect(jsonPath("$.conversionFactor").value(sameNumber(DEFAULT_CONVERSION_FACTOR)));
    }

    @Test
    @Transactional
    void getNonExistingUnitOfMeasureConversion() throws Exception {
        // Get the unitOfMeasureConversion
        restUnitOfMeasureConversionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitOfMeasureConversion() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();

        // Update the unitOfMeasureConversion
        UnitOfMeasureConversion updatedUnitOfMeasureConversion = unitOfMeasureConversionRepository
            .findById(unitOfMeasureConversion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUnitOfMeasureConversion are not directly saved in db
        em.detach(updatedUnitOfMeasureConversion);
        updatedUnitOfMeasureConversion.conversionFactor(UPDATED_CONVERSION_FACTOR);
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(updatedUnitOfMeasureConversion);

        restUnitOfMeasureConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasureConversionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasureConversion testUnitOfMeasureConversion = unitOfMeasureConversionList.get(unitOfMeasureConversionList.size() - 1);
        assertThat(testUnitOfMeasureConversion.getConversionFactor()).isEqualByComparingTo(UPDATED_CONVERSION_FACTOR);
    }

    @Test
    @Transactional
    void putNonExistingUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasureConversionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitOfMeasureConversionWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();

        // Update the unitOfMeasureConversion using partial update
        UnitOfMeasureConversion partialUpdatedUnitOfMeasureConversion = new UnitOfMeasureConversion();
        partialUpdatedUnitOfMeasureConversion.setId(unitOfMeasureConversion.getId());

        restUnitOfMeasureConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasureConversion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasureConversion))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasureConversion testUnitOfMeasureConversion = unitOfMeasureConversionList.get(unitOfMeasureConversionList.size() - 1);
        assertThat(testUnitOfMeasureConversion.getConversionFactor()).isEqualByComparingTo(DEFAULT_CONVERSION_FACTOR);
    }

    @Test
    @Transactional
    void fullUpdateUnitOfMeasureConversionWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();

        // Update the unitOfMeasureConversion using partial update
        UnitOfMeasureConversion partialUpdatedUnitOfMeasureConversion = new UnitOfMeasureConversion();
        partialUpdatedUnitOfMeasureConversion.setId(unitOfMeasureConversion.getId());

        partialUpdatedUnitOfMeasureConversion.conversionFactor(UPDATED_CONVERSION_FACTOR);

        restUnitOfMeasureConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasureConversion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasureConversion))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasureConversion testUnitOfMeasureConversion = unitOfMeasureConversionList.get(unitOfMeasureConversionList.size() - 1);
        assertThat(testUnitOfMeasureConversion.getConversionFactor()).isEqualByComparingTo(UPDATED_CONVERSION_FACTOR);
    }

    @Test
    @Transactional
    void patchNonExistingUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitOfMeasureConversionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitOfMeasureConversion() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureConversionRepository.findAll().size();
        unitOfMeasureConversion.setId(count.incrementAndGet());

        // Create the UnitOfMeasureConversion
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureConversionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasureConversionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasureConversion in the database
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitOfMeasureConversion() throws Exception {
        // Initialize the database
        unitOfMeasureConversionRepository.saveAndFlush(unitOfMeasureConversion);

        int databaseSizeBeforeDelete = unitOfMeasureConversionRepository.findAll().size();

        // Delete the unitOfMeasureConversion
        restUnitOfMeasureConversionMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitOfMeasureConversion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasureConversion> unitOfMeasureConversionList = unitOfMeasureConversionRepository.findAll();
        assertThat(unitOfMeasureConversionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
