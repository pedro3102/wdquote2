package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.DeliveryZone;
import com.sapwd.wdquote.repository.DeliveryZoneRepository;
import com.sapwd.wdquote.service.dto.DeliveryZoneDTO;
import com.sapwd.wdquote.service.mapper.DeliveryZoneMapper;
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
 * Integration tests for the {@link DeliveryZoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryZoneResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/delivery-zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryZoneRepository deliveryZoneRepository;

    @Autowired
    private DeliveryZoneMapper deliveryZoneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryZoneMockMvc;

    private DeliveryZone deliveryZone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryZone createEntity(EntityManager em) {
        DeliveryZone deliveryZone = new DeliveryZone().code(DEFAULT_CODE).name(DEFAULT_NAME);
        return deliveryZone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryZone createUpdatedEntity(EntityManager em) {
        DeliveryZone deliveryZone = new DeliveryZone().code(UPDATED_CODE).name(UPDATED_NAME);
        return deliveryZone;
    }

    @BeforeEach
    public void initTest() {
        deliveryZone = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryZone() throws Exception {
        int databaseSizeBeforeCreate = deliveryZoneRepository.findAll().size();
        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);
        restDeliveryZoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryZone testDeliveryZone = deliveryZoneList.get(deliveryZoneList.size() - 1);
        assertThat(testDeliveryZone.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDeliveryZone.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDeliveryZoneWithExistingId() throws Exception {
        // Create the DeliveryZone with an existing ID
        deliveryZone.setId(1L);
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        int databaseSizeBeforeCreate = deliveryZoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryZoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryZoneRepository.findAll().size();
        // set the field null
        deliveryZone.setCode(null);

        // Create the DeliveryZone, which fails.
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        restDeliveryZoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryZoneRepository.findAll().size();
        // set the field null
        deliveryZone.setName(null);

        // Create the DeliveryZone, which fails.
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        restDeliveryZoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryZones() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        // Get all the deliveryZoneList
        restDeliveryZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryZone.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDeliveryZone() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        // Get the deliveryZone
        restDeliveryZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryZone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryZone.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryZone() throws Exception {
        // Get the deliveryZone
        restDeliveryZoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeliveryZone() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();

        // Update the deliveryZone
        DeliveryZone updatedDeliveryZone = deliveryZoneRepository.findById(deliveryZone.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeliveryZone are not directly saved in db
        em.detach(updatedDeliveryZone);
        updatedDeliveryZone.code(UPDATED_CODE).name(UPDATED_NAME);
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(updatedDeliveryZone);

        restDeliveryZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
        DeliveryZone testDeliveryZone = deliveryZoneList.get(deliveryZoneList.size() - 1);
        assertThat(testDeliveryZone.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDeliveryZone.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryZoneWithPatch() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();

        // Update the deliveryZone using partial update
        DeliveryZone partialUpdatedDeliveryZone = new DeliveryZone();
        partialUpdatedDeliveryZone.setId(deliveryZone.getId());

        restDeliveryZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryZone))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
        DeliveryZone testDeliveryZone = deliveryZoneList.get(deliveryZoneList.size() - 1);
        assertThat(testDeliveryZone.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDeliveryZone.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryZoneWithPatch() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();

        // Update the deliveryZone using partial update
        DeliveryZone partialUpdatedDeliveryZone = new DeliveryZone();
        partialUpdatedDeliveryZone.setId(deliveryZone.getId());

        partialUpdatedDeliveryZone.code(UPDATED_CODE).name(UPDATED_NAME);

        restDeliveryZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryZone))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
        DeliveryZone testDeliveryZone = deliveryZoneList.get(deliveryZoneList.size() - 1);
        assertThat(testDeliveryZone.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDeliveryZone.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryZoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryZone() throws Exception {
        int databaseSizeBeforeUpdate = deliveryZoneRepository.findAll().size();
        deliveryZone.setId(count.incrementAndGet());

        // Create the DeliveryZone
        DeliveryZoneDTO deliveryZoneDTO = deliveryZoneMapper.toDto(deliveryZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryZoneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryZoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryZone in the database
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryZone() throws Exception {
        // Initialize the database
        deliveryZoneRepository.saveAndFlush(deliveryZone);

        int databaseSizeBeforeDelete = deliveryZoneRepository.findAll().size();

        // Delete the deliveryZone
        restDeliveryZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryZone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryZone> deliveryZoneList = deliveryZoneRepository.findAll();
        assertThat(deliveryZoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
