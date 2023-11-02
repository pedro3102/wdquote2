package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Product;
import com.sapwd.wdquote.domain.SystemModel;
import com.sapwd.wdquote.domain.Systema;
import com.sapwd.wdquote.repository.SystemModelRepository;
import com.sapwd.wdquote.service.dto.SystemModelDTO;
import com.sapwd.wdquote.service.mapper.SystemModelMapper;
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
 * Integration tests for the {@link SystemModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemModelRepository systemModelRepository;

    @Autowired
    private SystemModelMapper systemModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemModelMockMvc;

    private SystemModel systemModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemModel createEntity(EntityManager em) {
        SystemModel systemModel = new SystemModel().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).picture(DEFAULT_PICTURE);
        // Add required entity
        Systema systema;
        if (TestUtil.findAll(em, Systema.class).isEmpty()) {
            systema = SystemaResourceIT.createEntity(em);
            em.persist(systema);
            em.flush();
        } else {
            systema = TestUtil.findAll(em, Systema.class).get(0);
        }
        systemModel.setSystem(systema);
        return systemModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemModel createUpdatedEntity(EntityManager em) {
        SystemModel systemModel = new SystemModel().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).picture(UPDATED_PICTURE);
        // Add required entity
        Systema systema;
        if (TestUtil.findAll(em, Systema.class).isEmpty()) {
            systema = SystemaResourceIT.createUpdatedEntity(em);
            em.persist(systema);
            em.flush();
        } else {
            systema = TestUtil.findAll(em, Systema.class).get(0);
        }
        systemModel.setSystem(systema);
        return systemModel;
    }

    @BeforeEach
    public void initTest() {
        systemModel = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemModel() throws Exception {
        int databaseSizeBeforeCreate = systemModelRepository.findAll().size();
        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);
        restSystemModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeCreate + 1);
        SystemModel testSystemModel = systemModelList.get(systemModelList.size() - 1);
        assertThat(testSystemModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemModel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSystemModel.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void createSystemModelWithExistingId() throws Exception {
        // Create the SystemModel with an existing ID
        systemModel.setId(1L);
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        int databaseSizeBeforeCreate = systemModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemModelRepository.findAll().size();
        // set the field null
        systemModel.setName(null);

        // Create the SystemModel, which fails.
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        restSystemModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemModelRepository.findAll().size();
        // set the field null
        systemModel.setDescription(null);

        // Create the SystemModel, which fails.
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        restSystemModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemModels() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getSystemModel() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get the systemModel
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL_ID, systemModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getSystemModelsByIdFiltering() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        Long id = systemModel.getId();

        defaultSystemModelShouldBeFound("id.equals=" + id);
        defaultSystemModelShouldNotBeFound("id.notEquals=" + id);

        defaultSystemModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemModelShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemModelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where name equals to DEFAULT_NAME
        defaultSystemModelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the systemModelList where name equals to UPDATED_NAME
        defaultSystemModelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSystemModelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the systemModelList where name equals to UPDATED_NAME
        defaultSystemModelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where name is not null
        defaultSystemModelShouldBeFound("name.specified=true");

        // Get all the systemModelList where name is null
        defaultSystemModelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where name contains DEFAULT_NAME
        defaultSystemModelShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the systemModelList where name contains UPDATED_NAME
        defaultSystemModelShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where name does not contain DEFAULT_NAME
        defaultSystemModelShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the systemModelList where name does not contain UPDATED_NAME
        defaultSystemModelShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModelsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where description equals to DEFAULT_DESCRIPTION
        defaultSystemModelShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the systemModelList where description equals to UPDATED_DESCRIPTION
        defaultSystemModelShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemModelsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSystemModelShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the systemModelList where description equals to UPDATED_DESCRIPTION
        defaultSystemModelShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemModelsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where description is not null
        defaultSystemModelShouldBeFound("description.specified=true");

        // Get all the systemModelList where description is null
        defaultSystemModelShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemModelsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where description contains DEFAULT_DESCRIPTION
        defaultSystemModelShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the systemModelList where description contains UPDATED_DESCRIPTION
        defaultSystemModelShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemModelsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where description does not contain DEFAULT_DESCRIPTION
        defaultSystemModelShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the systemModelList where description does not contain UPDATED_DESCRIPTION
        defaultSystemModelShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemModelsByPictureIsEqualToSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where picture equals to DEFAULT_PICTURE
        defaultSystemModelShouldBeFound("picture.equals=" + DEFAULT_PICTURE);

        // Get all the systemModelList where picture equals to UPDATED_PICTURE
        defaultSystemModelShouldNotBeFound("picture.equals=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllSystemModelsByPictureIsInShouldWork() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where picture in DEFAULT_PICTURE or UPDATED_PICTURE
        defaultSystemModelShouldBeFound("picture.in=" + DEFAULT_PICTURE + "," + UPDATED_PICTURE);

        // Get all the systemModelList where picture equals to UPDATED_PICTURE
        defaultSystemModelShouldNotBeFound("picture.in=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllSystemModelsByPictureIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where picture is not null
        defaultSystemModelShouldBeFound("picture.specified=true");

        // Get all the systemModelList where picture is null
        defaultSystemModelShouldNotBeFound("picture.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemModelsByPictureContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where picture contains DEFAULT_PICTURE
        defaultSystemModelShouldBeFound("picture.contains=" + DEFAULT_PICTURE);

        // Get all the systemModelList where picture contains UPDATED_PICTURE
        defaultSystemModelShouldNotBeFound("picture.contains=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllSystemModelsByPictureNotContainsSomething() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        // Get all the systemModelList where picture does not contain DEFAULT_PICTURE
        defaultSystemModelShouldNotBeFound("picture.doesNotContain=" + DEFAULT_PICTURE);

        // Get all the systemModelList where picture does not contain UPDATED_PICTURE
        defaultSystemModelShouldBeFound("picture.doesNotContain=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllSystemModelsBySystemIsEqualToSomething() throws Exception {
        Systema system;
        if (TestUtil.findAll(em, Systema.class).isEmpty()) {
            systemModelRepository.saveAndFlush(systemModel);
            system = SystemaResourceIT.createEntity(em);
        } else {
            system = TestUtil.findAll(em, Systema.class).get(0);
        }
        em.persist(system);
        em.flush();
        systemModel.setSystem(system);
        systemModelRepository.saveAndFlush(systemModel);
        Long systemId = system.getId();

        // Get all the systemModelList where system equals to systemId
        defaultSystemModelShouldBeFound("systemId.equals=" + systemId);

        // Get all the systemModelList where system equals to (systemId + 1)
        defaultSystemModelShouldNotBeFound("systemId.equals=" + (systemId + 1));
    }

    @Test
    @Transactional
    void getAllSystemModelsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            systemModelRepository.saveAndFlush(systemModel);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        systemModel.addProduct(product);
        systemModelRepository.saveAndFlush(systemModel);
        Long productId = product.getId();

        // Get all the systemModelList where product equals to productId
        defaultSystemModelShouldBeFound("productId.equals=" + productId);

        // Get all the systemModelList where product equals to (productId + 1)
        defaultSystemModelShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemModelShouldBeFound(String filter) throws Exception {
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));

        // Check, that the count call also returns 1
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemModelShouldNotBeFound(String filter) throws Exception {
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemModel() throws Exception {
        // Get the systemModel
        restSystemModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemModel() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();

        // Update the systemModel
        SystemModel updatedSystemModel = systemModelRepository.findById(systemModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemModel are not directly saved in db
        em.detach(updatedSystemModel);
        updatedSystemModel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).picture(UPDATED_PICTURE);
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(updatedSystemModel);

        restSystemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
        SystemModel testSystemModel = systemModelList.get(systemModelList.size() - 1);
        assertThat(testSystemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSystemModel.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemModelWithPatch() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();

        // Update the systemModel using partial update
        SystemModel partialUpdatedSystemModel = new SystemModel();
        partialUpdatedSystemModel.setId(systemModel.getId());

        partialUpdatedSystemModel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSystemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemModel))
            )
            .andExpect(status().isOk());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
        SystemModel testSystemModel = systemModelList.get(systemModelList.size() - 1);
        assertThat(testSystemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSystemModel.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdateSystemModelWithPatch() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();

        // Update the systemModel using partial update
        SystemModel partialUpdatedSystemModel = new SystemModel();
        partialUpdatedSystemModel.setId(systemModel.getId());

        partialUpdatedSystemModel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).picture(UPDATED_PICTURE);

        restSystemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemModel))
            )
            .andExpect(status().isOk());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
        SystemModel testSystemModel = systemModelList.get(systemModelList.size() - 1);
        assertThat(testSystemModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSystemModel.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemModel() throws Exception {
        int databaseSizeBeforeUpdate = systemModelRepository.findAll().size();
        systemModel.setId(count.incrementAndGet());

        // Create the SystemModel
        SystemModelDTO systemModelDTO = systemModelMapper.toDto(systemModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(systemModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemModel in the database
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemModel() throws Exception {
        // Initialize the database
        systemModelRepository.saveAndFlush(systemModel);

        int databaseSizeBeforeDelete = systemModelRepository.findAll().size();

        // Delete the systemModel
        restSystemModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemModel> systemModelList = systemModelRepository.findAll();
        assertThat(systemModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
