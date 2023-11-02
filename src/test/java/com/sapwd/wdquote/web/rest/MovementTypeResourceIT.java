package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.MovementType;
import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.repository.MovementTypeRepository;
import com.sapwd.wdquote.service.dto.MovementTypeDTO;
import com.sapwd.wdquote.service.mapper.MovementTypeMapper;
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
 * Integration tests for the {@link MovementTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovementTypeResourceIT {

    private static final String DEFAULT_CODE = "AAA";
    private static final String UPDATED_CODE = "BBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final OperationType DEFAULT_TYPE = OperationType.IN;
    private static final OperationType UPDATED_TYPE = OperationType.OUT;

    private static final OperationCounterpart DEFAULT_COUNTERPART = OperationCounterpart.VENDOR;
    private static final OperationCounterpart UPDATED_COUNTERPART = OperationCounterpart.LOCATION;

    private static final String ENTITY_API_URL = "/api/movement-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovementTypeRepository movementTypeRepository;

    @Autowired
    private MovementTypeMapper movementTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovementTypeMockMvc;

    private MovementType movementType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementType createEntity(EntityManager em) {
        MovementType movementType = new MovementType()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .counterpart(DEFAULT_COUNTERPART);
        return movementType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementType createUpdatedEntity(EntityManager em) {
        MovementType movementType = new MovementType()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .counterpart(UPDATED_COUNTERPART);
        return movementType;
    }

    @BeforeEach
    public void initTest() {
        movementType = createEntity(em);
    }

    @Test
    @Transactional
    void createMovementType() throws Exception {
        int databaseSizeBeforeCreate = movementTypeRepository.findAll().size();
        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);
        restMovementTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MovementType testMovementType = movementTypeList.get(movementTypeList.size() - 1);
        assertThat(testMovementType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMovementType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMovementType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMovementType.getCounterpart()).isEqualTo(DEFAULT_COUNTERPART);
    }

    @Test
    @Transactional
    void createMovementTypeWithExistingId() throws Exception {
        // Create the MovementType with an existing ID
        movementType.setId(1L);
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        int databaseSizeBeforeCreate = movementTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovementTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementTypeRepository.findAll().size();
        // set the field null
        movementType.setCode(null);

        // Create the MovementType, which fails.
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        restMovementTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementTypeRepository.findAll().size();
        // set the field null
        movementType.setName(null);

        // Create the MovementType, which fails.
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        restMovementTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementTypeRepository.findAll().size();
        // set the field null
        movementType.setType(null);

        // Create the MovementType, which fails.
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        restMovementTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovementTypes() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        // Get all the movementTypeList
        restMovementTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movementType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].counterpart").value(hasItem(DEFAULT_COUNTERPART.toString())));
    }

    @Test
    @Transactional
    void getMovementType() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        // Get the movementType
        restMovementTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, movementType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movementType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.counterpart").value(DEFAULT_COUNTERPART.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMovementType() throws Exception {
        // Get the movementType
        restMovementTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovementType() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();

        // Update the movementType
        MovementType updatedMovementType = movementTypeRepository.findById(movementType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovementType are not directly saved in db
        em.detach(updatedMovementType);
        updatedMovementType.code(UPDATED_CODE).name(UPDATED_NAME).type(UPDATED_TYPE).counterpart(UPDATED_COUNTERPART);
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(updatedMovementType);

        restMovementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
        MovementType testMovementType = movementTypeList.get(movementTypeList.size() - 1);
        assertThat(testMovementType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMovementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMovementType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementType.getCounterpart()).isEqualTo(UPDATED_COUNTERPART);
    }

    @Test
    @Transactional
    void putNonExistingMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovementTypeWithPatch() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();

        // Update the movementType using partial update
        MovementType partialUpdatedMovementType = new MovementType();
        partialUpdatedMovementType.setId(movementType.getId());

        partialUpdatedMovementType.name(UPDATED_NAME).type(UPDATED_TYPE);

        restMovementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementType))
            )
            .andExpect(status().isOk());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
        MovementType testMovementType = movementTypeList.get(movementTypeList.size() - 1);
        assertThat(testMovementType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMovementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMovementType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementType.getCounterpart()).isEqualTo(DEFAULT_COUNTERPART);
    }

    @Test
    @Transactional
    void fullUpdateMovementTypeWithPatch() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();

        // Update the movementType using partial update
        MovementType partialUpdatedMovementType = new MovementType();
        partialUpdatedMovementType.setId(movementType.getId());

        partialUpdatedMovementType.code(UPDATED_CODE).name(UPDATED_NAME).type(UPDATED_TYPE).counterpart(UPDATED_COUNTERPART);

        restMovementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementType))
            )
            .andExpect(status().isOk());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
        MovementType testMovementType = movementTypeList.get(movementTypeList.size() - 1);
        assertThat(testMovementType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMovementType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMovementType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMovementType.getCounterpart()).isEqualTo(UPDATED_COUNTERPART);
    }

    @Test
    @Transactional
    void patchNonExistingMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movementTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovementType() throws Exception {
        int databaseSizeBeforeUpdate = movementTypeRepository.findAll().size();
        movementType.setId(count.incrementAndGet());

        // Create the MovementType
        MovementTypeDTO movementTypeDTO = movementTypeMapper.toDto(movementType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementType in the database
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovementType() throws Exception {
        // Initialize the database
        movementTypeRepository.saveAndFlush(movementType);

        int databaseSizeBeforeDelete = movementTypeRepository.findAll().size();

        // Delete the movementType
        restMovementTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, movementType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MovementType> movementTypeList = movementTypeRepository.findAll();
        assertThat(movementTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
