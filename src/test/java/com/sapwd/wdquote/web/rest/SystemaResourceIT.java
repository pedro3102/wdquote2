package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Systema;
import com.sapwd.wdquote.repository.SystemaRepository;
import com.sapwd.wdquote.service.dto.SystemaDTO;
import com.sapwd.wdquote.service.mapper.SystemaMapper;
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
 * Integration tests for the {@link SystemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/systemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemaRepository systemaRepository;

    @Autowired
    private SystemaMapper systemaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemaMockMvc;

    private Systema systema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systema createEntity(EntityManager em) {
        Systema systema = new Systema().name(DEFAULT_NAME).picture(DEFAULT_PICTURE);
        return systema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systema createUpdatedEntity(EntityManager em) {
        Systema systema = new Systema().name(UPDATED_NAME).picture(UPDATED_PICTURE);
        return systema;
    }

    @BeforeEach
    public void initTest() {
        systema = createEntity(em);
    }

    @Test
    @Transactional
    void createSystema() throws Exception {
        int databaseSizeBeforeCreate = systemaRepository.findAll().size();
        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);
        restSystemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemaDTO)))
            .andExpect(status().isCreated());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeCreate + 1);
        Systema testSystema = systemaList.get(systemaList.size() - 1);
        assertThat(testSystema.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystema.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void createSystemaWithExistingId() throws Exception {
        // Create the Systema with an existing ID
        systema.setId(1L);
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        int databaseSizeBeforeCreate = systemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemaRepository.findAll().size();
        // set the field null
        systema.setName(null);

        // Create the Systema, which fails.
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        restSystemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemaDTO)))
            .andExpect(status().isBadRequest());

        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemas() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        // Get all the systemaList
        restSystemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systema.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getSystema() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        // Get the systema
        restSystemaMockMvc
            .perform(get(ENTITY_API_URL_ID, systema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systema.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingSystema() throws Exception {
        // Get the systema
        restSystemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystema() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();

        // Update the systema
        Systema updatedSystema = systemaRepository.findById(systema.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystema are not directly saved in db
        em.detach(updatedSystema);
        updatedSystema.name(UPDATED_NAME).picture(UPDATED_PICTURE);
        SystemaDTO systemaDTO = systemaMapper.toDto(updatedSystema);

        restSystemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
        Systema testSystema = systemaList.get(systemaList.size() - 1);
        assertThat(testSystema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystema.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemaWithPatch() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();

        // Update the systema using partial update
        Systema partialUpdatedSystema = new Systema();
        partialUpdatedSystema.setId(systema.getId());

        partialUpdatedSystema.name(UPDATED_NAME);

        restSystemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystema))
            )
            .andExpect(status().isOk());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
        Systema testSystema = systemaList.get(systemaList.size() - 1);
        assertThat(testSystema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystema.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdateSystemaWithPatch() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();

        // Update the systema using partial update
        Systema partialUpdatedSystema = new Systema();
        partialUpdatedSystema.setId(systema.getId());

        partialUpdatedSystema.name(UPDATED_NAME).picture(UPDATED_PICTURE);

        restSystemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystema))
            )
            .andExpect(status().isOk());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
        Systema testSystema = systemaList.get(systemaList.size() - 1);
        assertThat(testSystema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystema.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystema() throws Exception {
        int databaseSizeBeforeUpdate = systemaRepository.findAll().size();
        systema.setId(count.incrementAndGet());

        // Create the Systema
        SystemaDTO systemaDTO = systemaMapper.toDto(systema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(systemaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Systema in the database
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystema() throws Exception {
        // Initialize the database
        systemaRepository.saveAndFlush(systema);

        int databaseSizeBeforeDelete = systemaRepository.findAll().size();

        // Delete the systema
        restSystemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, systema.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Systema> systemaList = systemaRepository.findAll();
        assertThat(systemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
