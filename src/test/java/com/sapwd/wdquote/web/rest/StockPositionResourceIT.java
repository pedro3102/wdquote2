package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.StockPosition;
import com.sapwd.wdquote.repository.StockPositionRepository;
import com.sapwd.wdquote.service.dto.StockPositionDTO;
import com.sapwd.wdquote.service.mapper.StockPositionMapper;
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
 * Integration tests for the {@link StockPositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockPositionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stock-positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockPositionRepository stockPositionRepository;

    @Autowired
    private StockPositionMapper stockPositionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockPositionMockMvc;

    private StockPosition stockPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockPosition createEntity(EntityManager em) {
        StockPosition stockPosition = new StockPosition().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return stockPosition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockPosition createUpdatedEntity(EntityManager em) {
        StockPosition stockPosition = new StockPosition().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return stockPosition;
    }

    @BeforeEach
    public void initTest() {
        stockPosition = createEntity(em);
    }

    @Test
    @Transactional
    void createStockPosition() throws Exception {
        int databaseSizeBeforeCreate = stockPositionRepository.findAll().size();
        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);
        restStockPositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeCreate + 1);
        StockPosition testStockPosition = stockPositionList.get(stockPositionList.size() - 1);
        assertThat(testStockPosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStockPosition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createStockPositionWithExistingId() throws Exception {
        // Create the StockPosition with an existing ID
        stockPosition.setId(1L);
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        int databaseSizeBeforeCreate = stockPositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockPositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPositionRepository.findAll().size();
        // set the field null
        stockPosition.setDescription(null);

        // Create the StockPosition, which fails.
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        restStockPositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockPositions() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        // Get all the stockPositionList
        restStockPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getStockPosition() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        // Get the stockPosition
        restStockPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, stockPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockPosition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingStockPosition() throws Exception {
        // Get the stockPosition
        restStockPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockPosition() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();

        // Update the stockPosition
        StockPosition updatedStockPosition = stockPositionRepository.findById(stockPosition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockPosition are not directly saved in db
        em.detach(updatedStockPosition);
        updatedStockPosition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(updatedStockPosition);

        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
        StockPosition testStockPosition = stockPositionList.get(stockPositionList.size() - 1);
        assertThat(testStockPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockPositionWithPatch() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();

        // Update the stockPosition using partial update
        StockPosition partialUpdatedStockPosition = new StockPosition();
        partialUpdatedStockPosition.setId(stockPosition.getId());

        partialUpdatedStockPosition.name(UPDATED_NAME);

        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockPosition))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
        StockPosition testStockPosition = stockPositionList.get(stockPositionList.size() - 1);
        assertThat(testStockPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockPosition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateStockPositionWithPatch() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();

        // Update the stockPosition using partial update
        StockPosition partialUpdatedStockPosition = new StockPosition();
        partialUpdatedStockPosition.setId(stockPosition.getId());

        partialUpdatedStockPosition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockPosition))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
        StockPosition testStockPosition = stockPositionList.get(stockPositionList.size() - 1);
        assertThat(testStockPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockPositionRepository.findAll().size();
        stockPosition.setId(count.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockPositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockPosition in the database
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockPosition() throws Exception {
        // Initialize the database
        stockPositionRepository.saveAndFlush(stockPosition);

        int databaseSizeBeforeDelete = stockPositionRepository.findAll().size();

        // Delete the stockPosition
        restStockPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockPosition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockPosition> stockPositionList = stockPositionRepository.findAll();
        assertThat(stockPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
