package com.sapwd.wdquote.web.rest;

import static com.sapwd.wdquote.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.MovementDetailsRepository;
import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
import com.sapwd.wdquote.service.mapper.MovementDetailsMapper;
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
 * Integration tests for the {@link MovementDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovementDetailsResourceIT {

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QTY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QTY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALE_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_VENDOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INVENTORY_QTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVENTORY_QTY = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVENTORY_QTY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/movement-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovementDetailsRepository movementDetailsRepository;

    @Autowired
    private MovementDetailsMapper movementDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovementDetailsMockMvc;

    private MovementDetails movementDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementDetails createEntity(EntityManager em) {
        MovementDetails movementDetails = new MovementDetails()
            .unitCost(DEFAULT_UNIT_COST)
            .qty(DEFAULT_QTY)
            .salePrice(DEFAULT_SALE_PRICE)
            .vendorCode(DEFAULT_VENDOR_CODE)
            .inventoryQty(DEFAULT_INVENTORY_QTY);
        // Add required entity
        Movement movement;
        if (TestUtil.findAll(em, Movement.class).isEmpty()) {
            movement = MovementResourceIT.createEntity(em);
            em.persist(movement);
            em.flush();
        } else {
            movement = TestUtil.findAll(em, Movement.class).get(0);
        }
        movementDetails.setMovement(movement);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        movementDetails.setProduct(product);
        // Add required entity
        Inventory inventory;
        if (TestUtil.findAll(em, Inventory.class).isEmpty()) {
            inventory = InventoryResourceIT.createEntity(em);
            em.persist(inventory);
            em.flush();
        } else {
            inventory = TestUtil.findAll(em, Inventory.class).get(0);
        }
        movementDetails.setInventory(inventory);
        return movementDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovementDetails createUpdatedEntity(EntityManager em) {
        MovementDetails movementDetails = new MovementDetails()
            .unitCost(UPDATED_UNIT_COST)
            .qty(UPDATED_QTY)
            .salePrice(UPDATED_SALE_PRICE)
            .vendorCode(UPDATED_VENDOR_CODE)
            .inventoryQty(UPDATED_INVENTORY_QTY);
        // Add required entity
        Movement movement;
        if (TestUtil.findAll(em, Movement.class).isEmpty()) {
            movement = MovementResourceIT.createUpdatedEntity(em);
            em.persist(movement);
            em.flush();
        } else {
            movement = TestUtil.findAll(em, Movement.class).get(0);
        }
        movementDetails.setMovement(movement);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        movementDetails.setProduct(product);
        // Add required entity
        Inventory inventory;
        if (TestUtil.findAll(em, Inventory.class).isEmpty()) {
            inventory = InventoryResourceIT.createUpdatedEntity(em);
            em.persist(inventory);
            em.flush();
        } else {
            inventory = TestUtil.findAll(em, Inventory.class).get(0);
        }
        movementDetails.setInventory(inventory);
        return movementDetails;
    }

    @BeforeEach
    public void initTest() {
        movementDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createMovementDetails() throws Exception {
        int databaseSizeBeforeCreate = movementDetailsRepository.findAll().size();
        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);
        restMovementDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        MovementDetails testMovementDetails = movementDetailsList.get(movementDetailsList.size() - 1);
        assertThat(testMovementDetails.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testMovementDetails.getQty()).isEqualByComparingTo(DEFAULT_QTY);
        assertThat(testMovementDetails.getSalePrice()).isEqualByComparingTo(DEFAULT_SALE_PRICE);
        assertThat(testMovementDetails.getVendorCode()).isEqualTo(DEFAULT_VENDOR_CODE);
        assertThat(testMovementDetails.getInventoryQty()).isEqualByComparingTo(DEFAULT_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void createMovementDetailsWithExistingId() throws Exception {
        // Create the MovementDetails with an existing ID
        movementDetails.setId(1L);
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        int databaseSizeBeforeCreate = movementDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovementDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementDetailsRepository.findAll().size();
        // set the field null
        movementDetails.setUnitCost(null);

        // Create the MovementDetails, which fails.
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        restMovementDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementDetailsRepository.findAll().size();
        // set the field null
        movementDetails.setQty(null);

        // Create the MovementDetails, which fails.
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        restMovementDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInventoryQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = movementDetailsRepository.findAll().size();
        // set the field null
        movementDetails.setInventoryQty(null);

        // Create the MovementDetails, which fails.
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        restMovementDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovementDetails() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movementDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(sameNumber(DEFAULT_SALE_PRICE))))
            .andExpect(jsonPath("$.[*].vendorCode").value(hasItem(DEFAULT_VENDOR_CODE)))
            .andExpect(jsonPath("$.[*].inventoryQty").value(hasItem(sameNumber(DEFAULT_INVENTORY_QTY))));
    }

    @Test
    @Transactional
    void getMovementDetails() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get the movementDetails
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, movementDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movementDetails.getId().intValue()))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.qty").value(sameNumber(DEFAULT_QTY)))
            .andExpect(jsonPath("$.salePrice").value(sameNumber(DEFAULT_SALE_PRICE)))
            .andExpect(jsonPath("$.vendorCode").value(DEFAULT_VENDOR_CODE))
            .andExpect(jsonPath("$.inventoryQty").value(sameNumber(DEFAULT_INVENTORY_QTY)));
    }

    @Test
    @Transactional
    void getMovementDetailsByIdFiltering() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        Long id = movementDetails.getId();

        defaultMovementDetailsShouldBeFound("id.equals=" + id);
        defaultMovementDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultMovementDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMovementDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultMovementDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMovementDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost equals to DEFAULT_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.equals=" + DEFAULT_UNIT_COST);

        // Get all the movementDetailsList where unitCost equals to UPDATED_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost in DEFAULT_UNIT_COST or UPDATED_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST);

        // Get all the movementDetailsList where unitCost equals to UPDATED_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.in=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost is not null
        defaultMovementDetailsShouldBeFound("unitCost.specified=true");

        // Get all the movementDetailsList where unitCost is null
        defaultMovementDetailsShouldNotBeFound("unitCost.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost is greater than or equal to DEFAULT_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.greaterThanOrEqual=" + DEFAULT_UNIT_COST);

        // Get all the movementDetailsList where unitCost is greater than or equal to UPDATED_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.greaterThanOrEqual=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost is less than or equal to DEFAULT_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.lessThanOrEqual=" + DEFAULT_UNIT_COST);

        // Get all the movementDetailsList where unitCost is less than or equal to SMALLER_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.lessThanOrEqual=" + SMALLER_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsLessThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost is less than DEFAULT_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.lessThan=" + DEFAULT_UNIT_COST);

        // Get all the movementDetailsList where unitCost is less than UPDATED_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.lessThan=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByUnitCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where unitCost is greater than DEFAULT_UNIT_COST
        defaultMovementDetailsShouldNotBeFound("unitCost.greaterThan=" + DEFAULT_UNIT_COST);

        // Get all the movementDetailsList where unitCost is greater than SMALLER_UNIT_COST
        defaultMovementDetailsShouldBeFound("unitCost.greaterThan=" + SMALLER_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty equals to DEFAULT_QTY
        defaultMovementDetailsShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the movementDetailsList where qty equals to UPDATED_QTY
        defaultMovementDetailsShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultMovementDetailsShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the movementDetailsList where qty equals to UPDATED_QTY
        defaultMovementDetailsShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty is not null
        defaultMovementDetailsShouldBeFound("qty.specified=true");

        // Get all the movementDetailsList where qty is null
        defaultMovementDetailsShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty is greater than or equal to DEFAULT_QTY
        defaultMovementDetailsShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the movementDetailsList where qty is greater than or equal to UPDATED_QTY
        defaultMovementDetailsShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty is less than or equal to DEFAULT_QTY
        defaultMovementDetailsShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the movementDetailsList where qty is less than or equal to SMALLER_QTY
        defaultMovementDetailsShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty is less than DEFAULT_QTY
        defaultMovementDetailsShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the movementDetailsList where qty is less than UPDATED_QTY
        defaultMovementDetailsShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where qty is greater than DEFAULT_QTY
        defaultMovementDetailsShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the movementDetailsList where qty is greater than SMALLER_QTY
        defaultMovementDetailsShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice equals to DEFAULT_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the movementDetailsList where salePrice equals to UPDATED_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the movementDetailsList where salePrice equals to UPDATED_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice is not null
        defaultMovementDetailsShouldBeFound("salePrice.specified=true");

        // Get all the movementDetailsList where salePrice is null
        defaultMovementDetailsShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice is greater than or equal to DEFAULT_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.greaterThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the movementDetailsList where salePrice is greater than or equal to UPDATED_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.greaterThanOrEqual=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice is less than or equal to DEFAULT_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.lessThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the movementDetailsList where salePrice is less than or equal to SMALLER_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.lessThanOrEqual=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice is less than DEFAULT_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.lessThan=" + DEFAULT_SALE_PRICE);

        // Get all the movementDetailsList where salePrice is less than UPDATED_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.lessThan=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsBySalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where salePrice is greater than DEFAULT_SALE_PRICE
        defaultMovementDetailsShouldNotBeFound("salePrice.greaterThan=" + DEFAULT_SALE_PRICE);

        // Get all the movementDetailsList where salePrice is greater than SMALLER_SALE_PRICE
        defaultMovementDetailsShouldBeFound("salePrice.greaterThan=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByVendorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where vendorCode equals to DEFAULT_VENDOR_CODE
        defaultMovementDetailsShouldBeFound("vendorCode.equals=" + DEFAULT_VENDOR_CODE);

        // Get all the movementDetailsList where vendorCode equals to UPDATED_VENDOR_CODE
        defaultMovementDetailsShouldNotBeFound("vendorCode.equals=" + UPDATED_VENDOR_CODE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByVendorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where vendorCode in DEFAULT_VENDOR_CODE or UPDATED_VENDOR_CODE
        defaultMovementDetailsShouldBeFound("vendorCode.in=" + DEFAULT_VENDOR_CODE + "," + UPDATED_VENDOR_CODE);

        // Get all the movementDetailsList where vendorCode equals to UPDATED_VENDOR_CODE
        defaultMovementDetailsShouldNotBeFound("vendorCode.in=" + UPDATED_VENDOR_CODE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByVendorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where vendorCode is not null
        defaultMovementDetailsShouldBeFound("vendorCode.specified=true");

        // Get all the movementDetailsList where vendorCode is null
        defaultMovementDetailsShouldNotBeFound("vendorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementDetailsByVendorCodeContainsSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where vendorCode contains DEFAULT_VENDOR_CODE
        defaultMovementDetailsShouldBeFound("vendorCode.contains=" + DEFAULT_VENDOR_CODE);

        // Get all the movementDetailsList where vendorCode contains UPDATED_VENDOR_CODE
        defaultMovementDetailsShouldNotBeFound("vendorCode.contains=" + UPDATED_VENDOR_CODE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByVendorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where vendorCode does not contain DEFAULT_VENDOR_CODE
        defaultMovementDetailsShouldNotBeFound("vendorCode.doesNotContain=" + DEFAULT_VENDOR_CODE);

        // Get all the movementDetailsList where vendorCode does not contain UPDATED_VENDOR_CODE
        defaultMovementDetailsShouldBeFound("vendorCode.doesNotContain=" + UPDATED_VENDOR_CODE);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty equals to DEFAULT_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.equals=" + DEFAULT_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty equals to UPDATED_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.equals=" + UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsInShouldWork() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty in DEFAULT_INVENTORY_QTY or UPDATED_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.in=" + DEFAULT_INVENTORY_QTY + "," + UPDATED_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty equals to UPDATED_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.in=" + UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty is not null
        defaultMovementDetailsShouldBeFound("inventoryQty.specified=true");

        // Get all the movementDetailsList where inventoryQty is null
        defaultMovementDetailsShouldNotBeFound("inventoryQty.specified=false");
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty is greater than or equal to DEFAULT_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.greaterThanOrEqual=" + DEFAULT_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty is greater than or equal to UPDATED_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.greaterThanOrEqual=" + UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty is less than or equal to DEFAULT_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.lessThanOrEqual=" + DEFAULT_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty is less than or equal to SMALLER_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.lessThanOrEqual=" + SMALLER_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty is less than DEFAULT_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.lessThan=" + DEFAULT_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty is less than UPDATED_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.lessThan=" + UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        // Get all the movementDetailsList where inventoryQty is greater than DEFAULT_INVENTORY_QTY
        defaultMovementDetailsShouldNotBeFound("inventoryQty.greaterThan=" + DEFAULT_INVENTORY_QTY);

        // Get all the movementDetailsList where inventoryQty is greater than SMALLER_INVENTORY_QTY
        defaultMovementDetailsShouldBeFound("inventoryQty.greaterThan=" + SMALLER_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void getAllMovementDetailsByMovementIsEqualToSomething() throws Exception {
        Movement movement;
        if (TestUtil.findAll(em, Movement.class).isEmpty()) {
            movementDetailsRepository.saveAndFlush(movementDetails);
            movement = MovementResourceIT.createEntity(em);
        } else {
            movement = TestUtil.findAll(em, Movement.class).get(0);
        }
        em.persist(movement);
        em.flush();
        movementDetails.setMovement(movement);
        movementDetailsRepository.saveAndFlush(movementDetails);
        Long movementId = movement.getId();

        // Get all the movementDetailsList where movement equals to movementId
        defaultMovementDetailsShouldBeFound("movementId.equals=" + movementId);

        // Get all the movementDetailsList where movement equals to (movementId + 1)
        defaultMovementDetailsShouldNotBeFound("movementId.equals=" + (movementId + 1));
    }

    @Test
    @Transactional
    void getAllMovementDetailsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            movementDetailsRepository.saveAndFlush(movementDetails);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        movementDetails.setProduct(product);
        movementDetailsRepository.saveAndFlush(movementDetails);
        Long productId = product.getId();

        // Get all the movementDetailsList where product equals to productId
        defaultMovementDetailsShouldBeFound("productId.equals=" + productId);

        // Get all the movementDetailsList where product equals to (productId + 1)
        defaultMovementDetailsShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllMovementDetailsByInventoryIsEqualToSomething() throws Exception {
        Inventory inventory;
        if (TestUtil.findAll(em, Inventory.class).isEmpty()) {
            movementDetailsRepository.saveAndFlush(movementDetails);
            inventory = InventoryResourceIT.createEntity(em);
        } else {
            inventory = TestUtil.findAll(em, Inventory.class).get(0);
        }
        em.persist(inventory);
        em.flush();
        movementDetails.setInventory(inventory);
        movementDetailsRepository.saveAndFlush(movementDetails);
        Long inventoryId = inventory.getId();

        // Get all the movementDetailsList where inventory equals to inventoryId
        defaultMovementDetailsShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the movementDetailsList where inventory equals to (inventoryId + 1)
        defaultMovementDetailsShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }

    @Test
    @Transactional
    void getAllMovementDetailsByStockPositionIsEqualToSomething() throws Exception {
        StockPosition stockPosition;
        if (TestUtil.findAll(em, StockPosition.class).isEmpty()) {
            movementDetailsRepository.saveAndFlush(movementDetails);
            stockPosition = StockPositionResourceIT.createEntity(em);
        } else {
            stockPosition = TestUtil.findAll(em, StockPosition.class).get(0);
        }
        em.persist(stockPosition);
        em.flush();
        movementDetails.setStockPosition(stockPosition);
        movementDetailsRepository.saveAndFlush(movementDetails);
        Long stockPositionId = stockPosition.getId();

        // Get all the movementDetailsList where stockPosition equals to stockPositionId
        defaultMovementDetailsShouldBeFound("stockPositionId.equals=" + stockPositionId);

        // Get all the movementDetailsList where stockPosition equals to (stockPositionId + 1)
        defaultMovementDetailsShouldNotBeFound("stockPositionId.equals=" + (stockPositionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovementDetailsShouldBeFound(String filter) throws Exception {
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movementDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(sameNumber(DEFAULT_SALE_PRICE))))
            .andExpect(jsonPath("$.[*].vendorCode").value(hasItem(DEFAULT_VENDOR_CODE)))
            .andExpect(jsonPath("$.[*].inventoryQty").value(hasItem(sameNumber(DEFAULT_INVENTORY_QTY))));

        // Check, that the count call also returns 1
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovementDetailsShouldNotBeFound(String filter) throws Exception {
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovementDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMovementDetails() throws Exception {
        // Get the movementDetails
        restMovementDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovementDetails() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();

        // Update the movementDetails
        MovementDetails updatedMovementDetails = movementDetailsRepository.findById(movementDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovementDetails are not directly saved in db
        em.detach(updatedMovementDetails);
        updatedMovementDetails
            .unitCost(UPDATED_UNIT_COST)
            .qty(UPDATED_QTY)
            .salePrice(UPDATED_SALE_PRICE)
            .vendorCode(UPDATED_VENDOR_CODE)
            .inventoryQty(UPDATED_INVENTORY_QTY);
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(updatedMovementDetails);

        restMovementDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
        MovementDetails testMovementDetails = movementDetailsList.get(movementDetailsList.size() - 1);
        assertThat(testMovementDetails.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testMovementDetails.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testMovementDetails.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testMovementDetails.getVendorCode()).isEqualTo(UPDATED_VENDOR_CODE);
        assertThat(testMovementDetails.getInventoryQty()).isEqualByComparingTo(UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void putNonExistingMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movementDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovementDetailsWithPatch() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();

        // Update the movementDetails using partial update
        MovementDetails partialUpdatedMovementDetails = new MovementDetails();
        partialUpdatedMovementDetails.setId(movementDetails.getId());

        partialUpdatedMovementDetails
            .unitCost(UPDATED_UNIT_COST)
            .qty(UPDATED_QTY)
            .salePrice(UPDATED_SALE_PRICE)
            .vendorCode(UPDATED_VENDOR_CODE);

        restMovementDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementDetails))
            )
            .andExpect(status().isOk());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
        MovementDetails testMovementDetails = movementDetailsList.get(movementDetailsList.size() - 1);
        assertThat(testMovementDetails.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testMovementDetails.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testMovementDetails.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testMovementDetails.getVendorCode()).isEqualTo(UPDATED_VENDOR_CODE);
        assertThat(testMovementDetails.getInventoryQty()).isEqualByComparingTo(DEFAULT_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void fullUpdateMovementDetailsWithPatch() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();

        // Update the movementDetails using partial update
        MovementDetails partialUpdatedMovementDetails = new MovementDetails();
        partialUpdatedMovementDetails.setId(movementDetails.getId());

        partialUpdatedMovementDetails
            .unitCost(UPDATED_UNIT_COST)
            .qty(UPDATED_QTY)
            .salePrice(UPDATED_SALE_PRICE)
            .vendorCode(UPDATED_VENDOR_CODE)
            .inventoryQty(UPDATED_INVENTORY_QTY);

        restMovementDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovementDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovementDetails))
            )
            .andExpect(status().isOk());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
        MovementDetails testMovementDetails = movementDetailsList.get(movementDetailsList.size() - 1);
        assertThat(testMovementDetails.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testMovementDetails.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testMovementDetails.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testMovementDetails.getVendorCode()).isEqualTo(UPDATED_VENDOR_CODE);
        assertThat(testMovementDetails.getInventoryQty()).isEqualByComparingTo(UPDATED_INVENTORY_QTY);
    }

    @Test
    @Transactional
    void patchNonExistingMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movementDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovementDetails() throws Exception {
        int databaseSizeBeforeUpdate = movementDetailsRepository.findAll().size();
        movementDetails.setId(count.incrementAndGet());

        // Create the MovementDetails
        MovementDetailsDTO movementDetailsDTO = movementDetailsMapper.toDto(movementDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovementDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movementDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MovementDetails in the database
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovementDetails() throws Exception {
        // Initialize the database
        movementDetailsRepository.saveAndFlush(movementDetails);

        int databaseSizeBeforeDelete = movementDetailsRepository.findAll().size();

        // Delete the movementDetails
        restMovementDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, movementDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MovementDetails> movementDetailsList = movementDetailsRepository.findAll();
        assertThat(movementDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
