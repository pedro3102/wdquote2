package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import com.sapwd.wdquote.repository.MovementRepository;
import com.sapwd.wdquote.service.dto.MovementDTO;
import com.sapwd.wdquote.service.mapper.MovementMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MovementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovementResourceIT {

    private static final String DEFAULT_NO = "AAAAAAAA";
    private static final String UPDATED_NO = "BBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CANCELED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCELED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCELED_DATE = LocalDate.ofEpochDay(-1L);

    private static final MovementStatus DEFAULT_STATUS = MovementStatus.PENDING;
    private static final MovementStatus UPDATED_STATUS = MovementStatus.COMPLETED;

    private static final String ENTITY_API_URL = "/api/movements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private MovementMapper movementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovementMockMvc;

    private Movement movement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movement createEntity(EntityManager em) {
        Movement movement = new Movement()
            .no(DEFAULT_NO)
            .reference(DEFAULT_REFERENCE)
            .date(DEFAULT_DATE)
            .note(DEFAULT_NOTE)
            .canceledDate(DEFAULT_CANCELED_DATE)
            .status(DEFAULT_STATUS);
        // Add required entity
        MovementType movementType;
        if (TestUtil.findAll(em, MovementType.class).isEmpty()) {
            movementType = MovementTypeResourceIT.createEntity(em);
            em.persist(movementType);
            em.flush();
        } else {
            movementType = TestUtil.findAll(em, MovementType.class).get(0);
        }
        movement.setMovementType(movementType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        movement.setLocation(location);
        return movement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movement createUpdatedEntity(EntityManager em) {
        Movement movement = new Movement()
            .no(UPDATED_NO)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .canceledDate(UPDATED_CANCELED_DATE)
            .status(UPDATED_STATUS);
        // Add required entity
        MovementType movementType;
        if (TestUtil.findAll(em, MovementType.class).isEmpty()) {
            movementType = MovementTypeResourceIT.createUpdatedEntity(em);
            em.persist(movementType);
            em.flush();
        } else {
            movementType = TestUtil.findAll(em, MovementType.class).get(0);
        }
        movement.setMovementType(movementType);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        movement.setLocation(location);
        return movement;
    }

    @BeforeEach
    public void initTest() {
        movement = createEntity(em);
    }

    @Test
    @Transactional
    void createMovement() throws Exception {
        int databaseSizeBeforeCreate = movementRepository.findAll().size();
        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);
        restMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDTO)))
            .andExpect(status().isCreated());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeCreate + 1);
        Movement testMovement = movementList.get(movementList.size() - 1);
        assertThat(testMovement.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testMovement.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMovement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMovement.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testMovement.getCanceledDate()).isEqualTo(DEFAULT_CANCELED_DATE);
        assertThat(testMovement.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createMovementWithExistingId() throws Exception {
        // Create the Movement with an existing ID
        movement.setId(1L);
        MovementDTO movementDTO = movementMapper.toDto(movement);

        int databaseSizeBeforeCreate = movementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementRepository.findAll().size();
        // set the field null
        movement.setNo(null);

        // Create the Movement, which fails.
        MovementDTO movementDTO = movementMapper.toDto(movement);

        restMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDTO)))
            .andExpect(status().isBadRequest());

        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementRepository.findAll().size();
        // set the field null
        movement.setDate(null);

        // Create the Movement, which fails.
        MovementDTO movementDTO = movementMapper.toDto(movement);

        restMovementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDTO)))
            .andExpect(status().isBadRequest());

        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovements() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList
        restMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movement.getId().intValue())))
            .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].canceledDate").value(hasItem(DEFAULT_CANCELED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getMovement() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get the movement
        restMovementMockMvc
            .perform(get(ENTITY_API_URL_ID, movement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movement.getId().intValue()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.canceledDate").value(DEFAULT_CANCELED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getMovementsByIdFiltering() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        Long id = movement.getId();

        defaultMovementShouldBeFound("id.equals=" + id);
        defaultMovementShouldNotBeFound("id.notEquals=" + id);

        defaultMovementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMovementShouldNotBeFound("id.greaterThan=" + id);

        defaultMovementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMovementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMovementsByNoIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where no equals to DEFAULT_NO
        defaultMovementShouldBeFound("no.equals=" + DEFAULT_NO);

        // Get all the movementList where no equals to UPDATED_NO
        defaultMovementShouldNotBeFound("no.equals=" + UPDATED_NO);
    }

    @Test
    @Transactional
    void getAllMovementsByNoIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where no in DEFAULT_NO or UPDATED_NO
        defaultMovementShouldBeFound("no.in=" + DEFAULT_NO + "," + UPDATED_NO);

        // Get all the movementList where no equals to UPDATED_NO
        defaultMovementShouldNotBeFound("no.in=" + UPDATED_NO);
    }

    @Test
    @Transactional
    void getAllMovementsByNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where no is not null
        defaultMovementShouldBeFound("no.specified=true");

        // Get all the movementList where no is null
        defaultMovementShouldNotBeFound("no.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByNoContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where no contains DEFAULT_NO
        defaultMovementShouldBeFound("no.contains=" + DEFAULT_NO);

        // Get all the movementList where no contains UPDATED_NO
        defaultMovementShouldNotBeFound("no.contains=" + UPDATED_NO);
    }

    @Test
    @Transactional
    void getAllMovementsByNoNotContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where no does not contain DEFAULT_NO
        defaultMovementShouldNotBeFound("no.doesNotContain=" + DEFAULT_NO);

        // Get all the movementList where no does not contain UPDATED_NO
        defaultMovementShouldBeFound("no.doesNotContain=" + UPDATED_NO);
    }

    @Test
    @Transactional
    void getAllMovementsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where reference equals to DEFAULT_REFERENCE
        defaultMovementShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the movementList where reference equals to UPDATED_REFERENCE
        defaultMovementShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMovementsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultMovementShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the movementList where reference equals to UPDATED_REFERENCE
        defaultMovementShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMovementsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where reference is not null
        defaultMovementShouldBeFound("reference.specified=true");

        // Get all the movementList where reference is null
        defaultMovementShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where reference contains DEFAULT_REFERENCE
        defaultMovementShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the movementList where reference contains UPDATED_REFERENCE
        defaultMovementShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMovementsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where reference does not contain DEFAULT_REFERENCE
        defaultMovementShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the movementList where reference does not contain UPDATED_REFERENCE
        defaultMovementShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date equals to DEFAULT_DATE
        defaultMovementShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the movementList where date equals to UPDATED_DATE
        defaultMovementShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMovementShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the movementList where date equals to UPDATED_DATE
        defaultMovementShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date is not null
        defaultMovementShouldBeFound("date.specified=true");

        // Get all the movementList where date is null
        defaultMovementShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date is greater than or equal to DEFAULT_DATE
        defaultMovementShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the movementList where date is greater than or equal to UPDATED_DATE
        defaultMovementShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date is less than or equal to DEFAULT_DATE
        defaultMovementShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the movementList where date is less than or equal to SMALLER_DATE
        defaultMovementShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date is less than DEFAULT_DATE
        defaultMovementShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the movementList where date is less than UPDATED_DATE
        defaultMovementShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where date is greater than DEFAULT_DATE
        defaultMovementShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the movementList where date is greater than SMALLER_DATE
        defaultMovementShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where note equals to DEFAULT_NOTE
        defaultMovementShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the movementList where note equals to UPDATED_NOTE
        defaultMovementShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMovementsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultMovementShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the movementList where note equals to UPDATED_NOTE
        defaultMovementShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMovementsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where note is not null
        defaultMovementShouldBeFound("note.specified=true");

        // Get all the movementList where note is null
        defaultMovementShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByNoteContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where note contains DEFAULT_NOTE
        defaultMovementShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the movementList where note contains UPDATED_NOTE
        defaultMovementShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMovementsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where note does not contain DEFAULT_NOTE
        defaultMovementShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the movementList where note does not contain UPDATED_NOTE
        defaultMovementShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate equals to DEFAULT_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.equals=" + DEFAULT_CANCELED_DATE);

        // Get all the movementList where canceledDate equals to UPDATED_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.equals=" + UPDATED_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate in DEFAULT_CANCELED_DATE or UPDATED_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.in=" + DEFAULT_CANCELED_DATE + "," + UPDATED_CANCELED_DATE);

        // Get all the movementList where canceledDate equals to UPDATED_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.in=" + UPDATED_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate is not null
        defaultMovementShouldBeFound("canceledDate.specified=true");

        // Get all the movementList where canceledDate is null
        defaultMovementShouldNotBeFound("canceledDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate is greater than or equal to DEFAULT_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.greaterThanOrEqual=" + DEFAULT_CANCELED_DATE);

        // Get all the movementList where canceledDate is greater than or equal to UPDATED_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.greaterThanOrEqual=" + UPDATED_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate is less than or equal to DEFAULT_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.lessThanOrEqual=" + DEFAULT_CANCELED_DATE);

        // Get all the movementList where canceledDate is less than or equal to SMALLER_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.lessThanOrEqual=" + SMALLER_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsLessThanSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate is less than DEFAULT_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.lessThan=" + DEFAULT_CANCELED_DATE);

        // Get all the movementList where canceledDate is less than UPDATED_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.lessThan=" + UPDATED_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByCanceledDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where canceledDate is greater than DEFAULT_CANCELED_DATE
        defaultMovementShouldNotBeFound("canceledDate.greaterThan=" + DEFAULT_CANCELED_DATE);

        // Get all the movementList where canceledDate is greater than SMALLER_CANCELED_DATE
        defaultMovementShouldBeFound("canceledDate.greaterThan=" + SMALLER_CANCELED_DATE);
    }

    @Test
    @Transactional
    void getAllMovementsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where status equals to DEFAULT_STATUS
        defaultMovementShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the movementList where status equals to UPDATED_STATUS
        defaultMovementShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMovementsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMovementShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the movementList where status equals to UPDATED_STATUS
        defaultMovementShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMovementsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        // Get all the movementList where status is not null
        defaultMovementShouldBeFound("status.specified=true");

        // Get all the movementList where status is null
        defaultMovementShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementsByMovementTypeIsEqualToSomething() throws Exception {
        MovementType movementType;
        if (TestUtil.findAll(em, MovementType.class).isEmpty()) {
            movementRepository.saveAndFlush(movement);
            movementType = MovementTypeResourceIT.createEntity(em);
        } else {
            movementType = TestUtil.findAll(em, MovementType.class).get(0);
        }
        em.persist(movementType);
        em.flush();
        movement.setMovementType(movementType);
        movementRepository.saveAndFlush(movement);
        Long movementTypeId = movementType.getId();

        // Get all the movementList where movementType equals to movementTypeId
        defaultMovementShouldBeFound("movementTypeId.equals=" + movementTypeId);

        // Get all the movementList where movementType equals to (movementTypeId + 1)
        defaultMovementShouldNotBeFound("movementTypeId.equals=" + (movementTypeId + 1));
    }

    @Test
    @Transactional
    void getAllMovementsByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            movementRepository.saveAndFlush(movement);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        movement.setLocation(location);
        movementRepository.saveAndFlush(movement);
        Long locationId = location.getId();

        // Get all the movementList where location equals to locationId
        defaultMovementShouldBeFound("locationId.equals=" + locationId);

        // Get all the movementList where location equals to (locationId + 1)
        defaultMovementShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllMovementsByCounterpartLocationIsEqualToSomething() throws Exception {
        Location counterpartLocation;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            movementRepository.saveAndFlush(movement);
            counterpartLocation = LocationResourceIT.createEntity(em);
        } else {
            counterpartLocation = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(counterpartLocation);
        em.flush();
        movement.setCounterpartLocation(counterpartLocation);
        movementRepository.saveAndFlush(movement);
        Long counterpartLocationId = counterpartLocation.getId();

        // Get all the movementList where counterpartLocation equals to counterpartLocationId
        defaultMovementShouldBeFound("counterpartLocationId.equals=" + counterpartLocationId);

        // Get all the movementList where counterpartLocation equals to (counterpartLocationId + 1)
        defaultMovementShouldNotBeFound("counterpartLocationId.equals=" + (counterpartLocationId + 1));
    }

    @Test
    @Transactional
    void getAllMovementsByCounterpartVendorIsEqualToSomething() throws Exception {
        Vendor counterpartVendor;
        if (TestUtil.findAll(em, Vendor.class).isEmpty()) {
            movementRepository.saveAndFlush(movement);
            counterpartVendor = VendorResourceIT.createEntity(em);
        } else {
            counterpartVendor = TestUtil.findAll(em, Vendor.class).get(0);
        }
        em.persist(counterpartVendor);
        em.flush();
        movement.setCounterpartVendor(counterpartVendor);
        movementRepository.saveAndFlush(movement);
        Long counterpartVendorId = counterpartVendor.getId();

        // Get all the movementList where counterpartVendor equals to counterpartVendorId
        defaultMovementShouldBeFound("counterpartVendorId.equals=" + counterpartVendorId);

        // Get all the movementList where counterpartVendor equals to (counterpartVendorId + 1)
        defaultMovementShouldNotBeFound("counterpartVendorId.equals=" + (counterpartVendorId + 1));
    }

    @Test
    @Transactional
    void getAllMovementsByCounterpartCustomerIsEqualToSomething() throws Exception {
        Customer counterpartCustomer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            movementRepository.saveAndFlush(movement);
            counterpartCustomer = CustomerResourceIT.createEntity(em);
        } else {
            counterpartCustomer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(counterpartCustomer);
        em.flush();
        movement.setCounterpartCustomer(counterpartCustomer);
        movementRepository.saveAndFlush(movement);
        Long counterpartCustomerId = counterpartCustomer.getId();

        // Get all the movementList where counterpartCustomer equals to counterpartCustomerId
        defaultMovementShouldBeFound("counterpartCustomerId.equals=" + counterpartCustomerId);

        // Get all the movementList where counterpartCustomer equals to (counterpartCustomerId + 1)
        defaultMovementShouldNotBeFound("counterpartCustomerId.equals=" + (counterpartCustomerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovementShouldBeFound(String filter) throws Exception {
        restMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movement.getId().intValue())))
            .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].canceledDate").value(hasItem(DEFAULT_CANCELED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restMovementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovementShouldNotBeFound(String filter) throws Exception {
        restMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMovement() throws Exception {
        // Get the movement
        restMovementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovement() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        int databaseSizeBeforeUpdate = movementRepository.findAll().size();

        // Update the movement
        Movement updatedMovement = movementRepository.findById(movement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovement are not directly saved in db
        em.detach(updatedMovement);
        updatedMovement
            .no(UPDATED_NO)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .canceledDate(UPDATED_CANCELED_DATE)
            .status(UPDATED_STATUS);
        MovementDTO movementDTO = movementMapper.toDto(updatedMovement);

        restMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
        Movement testMovement = movementList.get(movementList.size() - 1);
        assertThat(testMovement.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testMovement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMovement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMovement.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testMovement.getCanceledDate()).isEqualTo(UPDATED_CANCELED_DATE);
        assertThat(testMovement.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovementWithPatch() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        int databaseSizeBeforeUpdate = movementRepository.findAll().size();

        // Update the movement using partial update
        Movement partialUpdatedMovement = new Movement();
        partialUpdatedMovement.setId(movement.getId());

        partialUpdatedMovement.reference(UPDATED_REFERENCE).canceledDate(UPDATED_CANCELED_DATE).status(UPDATED_STATUS);

        restMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovement))
            )
            .andExpect(status().isOk());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
        Movement testMovement = movementList.get(movementList.size() - 1);
        assertThat(testMovement.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testMovement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMovement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMovement.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testMovement.getCanceledDate()).isEqualTo(UPDATED_CANCELED_DATE);
        assertThat(testMovement.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateMovementWithPatch() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        int databaseSizeBeforeUpdate = movementRepository.findAll().size();

        // Update the movement using partial update
        Movement partialUpdatedMovement = new Movement();
        partialUpdatedMovement.setId(movement.getId());

        partialUpdatedMovement
            .no(UPDATED_NO)
            .reference(UPDATED_REFERENCE)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE)
            .canceledDate(UPDATED_CANCELED_DATE)
            .status(UPDATED_STATUS);

        restMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovement))
            )
            .andExpect(status().isOk());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
        Movement testMovement = movementList.get(movementList.size() - 1);
        assertThat(testMovement.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testMovement.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMovement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMovement.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testMovement.getCanceledDate()).isEqualTo(UPDATED_CANCELED_DATE);
        assertThat(testMovement.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovement() throws Exception {
        int databaseSizeBeforeUpdate = movementRepository.findAll().size();
        movement.setId(count.incrementAndGet());

        // Create the Movement
        MovementDTO movementDTO = movementMapper.toDto(movement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(movementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movement in the database
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovement() throws Exception {
        // Initialize the database
        movementRepository.saveAndFlush(movement);

        int databaseSizeBeforeDelete = movementRepository.findAll().size();

        // Delete the movement
        restMovementMockMvc
            .perform(delete(ENTITY_API_URL_ID, movement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movement> movementList = movementRepository.findAll();
        assertThat(movementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
