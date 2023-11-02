package com.sapwd.wdquote.web.rest;

import static com.sapwd.wdquote.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Inventory;
import com.sapwd.wdquote.domain.Location;
import com.sapwd.wdquote.domain.Product;
import com.sapwd.wdquote.repository.InventoryRepository;
import com.sapwd.wdquote.service.dto.InventoryDTO;
import com.sapwd.wdquote.service.mapper.InventoryMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryResourceIT {

    private static final BigDecimal DEFAULT_QTY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QTY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QTY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_COST = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_LAST_ACTIVITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACTIVITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_ACTIVITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SHELF = "AAAAAAAAAA";
    private static final String UPDATED_SHELF = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REORDER_POINT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REORDER_POINT = new BigDecimal(2);
    private static final BigDecimal SMALLER_REORDER_POINT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_VENDOR_LEAD_TIME = 1;
    private static final Integer UPDATED_VENDOR_LEAD_TIME = 2;
    private static final Integer SMALLER_VENDOR_LEAD_TIME = 1 - 1;

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .qty(DEFAULT_QTY)
            .unitCost(DEFAULT_UNIT_COST)
            .lastActivityDate(DEFAULT_LAST_ACTIVITY_DATE)
            .shelf(DEFAULT_SHELF)
            .reorderPoint(DEFAULT_REORDER_POINT)
            .vendorLeadTime(DEFAULT_VENDOR_LEAD_TIME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        inventory.setLocation(location);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        inventory.setProduct(product);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory inventory = new Inventory()
            .qty(UPDATED_QTY)
            .unitCost(UPDATED_UNIT_COST)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE)
            .shelf(UPDATED_SHELF)
            .reorderPoint(UPDATED_REORDER_POINT)
            .vendorLeadTime(UPDATED_VENDOR_LEAD_TIME);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        inventory.setLocation(location);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        inventory.setProduct(product);
        return inventory;
    }

    @BeforeEach
    public void initTest() {
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();
        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQty()).isEqualByComparingTo(DEFAULT_QTY);
        assertThat(testInventory.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testInventory.getLastActivityDate()).isEqualTo(DEFAULT_LAST_ACTIVITY_DATE);
        assertThat(testInventory.getShelf()).isEqualTo(DEFAULT_SHELF);
        assertThat(testInventory.getReorderPoint()).isEqualByComparingTo(DEFAULT_REORDER_POINT);
        assertThat(testInventory.getVendorLeadTime()).isEqualTo(DEFAULT_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventory.setId(1L);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setQty(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setUnitCost(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastActivityDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setLastActivityDate(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].lastActivityDate").value(hasItem(DEFAULT_LAST_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].shelf").value(hasItem(DEFAULT_SHELF)))
            .andExpect(jsonPath("$.[*].reorderPoint").value(hasItem(sameNumber(DEFAULT_REORDER_POINT))))
            .andExpect(jsonPath("$.[*].vendorLeadTime").value(hasItem(DEFAULT_VENDOR_LEAD_TIME)));
    }

    @Test
    @Transactional
    void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.qty").value(sameNumber(DEFAULT_QTY)))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.lastActivityDate").value(DEFAULT_LAST_ACTIVITY_DATE.toString()))
            .andExpect(jsonPath("$.shelf").value(DEFAULT_SHELF))
            .andExpect(jsonPath("$.reorderPoint").value(sameNumber(DEFAULT_REORDER_POINT)))
            .andExpect(jsonPath("$.vendorLeadTime").value(DEFAULT_VENDOR_LEAD_TIME));
    }

    @Test
    @Transactional
    void getInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        Long id = inventory.getId();

        defaultInventoryShouldBeFound("id.equals=" + id);
        defaultInventoryShouldNotBeFound("id.notEquals=" + id);

        defaultInventoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInventoryShouldNotBeFound("id.greaterThan=" + id);

        defaultInventoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInventoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty equals to DEFAULT_QTY
        defaultInventoryShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the inventoryList where qty equals to UPDATED_QTY
        defaultInventoryShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultInventoryShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the inventoryList where qty equals to UPDATED_QTY
        defaultInventoryShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty is not null
        defaultInventoryShouldBeFound("qty.specified=true");

        // Get all the inventoryList where qty is null
        defaultInventoryShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty is greater than or equal to DEFAULT_QTY
        defaultInventoryShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the inventoryList where qty is greater than or equal to UPDATED_QTY
        defaultInventoryShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty is less than or equal to DEFAULT_QTY
        defaultInventoryShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the inventoryList where qty is less than or equal to SMALLER_QTY
        defaultInventoryShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty is less than DEFAULT_QTY
        defaultInventoryShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the inventoryList where qty is less than UPDATED_QTY
        defaultInventoryShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where qty is greater than DEFAULT_QTY
        defaultInventoryShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the inventoryList where qty is greater than SMALLER_QTY
        defaultInventoryShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost equals to DEFAULT_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.equals=" + DEFAULT_UNIT_COST);

        // Get all the inventoryList where unitCost equals to UPDATED_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost in DEFAULT_UNIT_COST or UPDATED_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST);

        // Get all the inventoryList where unitCost equals to UPDATED_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.in=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost is not null
        defaultInventoryShouldBeFound("unitCost.specified=true");

        // Get all the inventoryList where unitCost is null
        defaultInventoryShouldNotBeFound("unitCost.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost is greater than or equal to DEFAULT_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.greaterThanOrEqual=" + DEFAULT_UNIT_COST);

        // Get all the inventoryList where unitCost is greater than or equal to UPDATED_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.greaterThanOrEqual=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost is less than or equal to DEFAULT_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.lessThanOrEqual=" + DEFAULT_UNIT_COST);

        // Get all the inventoryList where unitCost is less than or equal to SMALLER_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.lessThanOrEqual=" + SMALLER_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsLessThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost is less than DEFAULT_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.lessThan=" + DEFAULT_UNIT_COST);

        // Get all the inventoryList where unitCost is less than UPDATED_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.lessThan=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByUnitCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where unitCost is greater than DEFAULT_UNIT_COST
        defaultInventoryShouldNotBeFound("unitCost.greaterThan=" + DEFAULT_UNIT_COST);

        // Get all the inventoryList where unitCost is greater than SMALLER_UNIT_COST
        defaultInventoryShouldBeFound("unitCost.greaterThan=" + SMALLER_UNIT_COST);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate equals to DEFAULT_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.equals=" + DEFAULT_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate equals to UPDATED_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.equals=" + UPDATED_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate in DEFAULT_LAST_ACTIVITY_DATE or UPDATED_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.in=" + DEFAULT_LAST_ACTIVITY_DATE + "," + UPDATED_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate equals to UPDATED_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.in=" + UPDATED_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate is not null
        defaultInventoryShouldBeFound("lastActivityDate.specified=true");

        // Get all the inventoryList where lastActivityDate is null
        defaultInventoryShouldNotBeFound("lastActivityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate is greater than or equal to DEFAULT_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.greaterThanOrEqual=" + DEFAULT_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate is greater than or equal to UPDATED_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.greaterThanOrEqual=" + UPDATED_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate is less than or equal to DEFAULT_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.lessThanOrEqual=" + DEFAULT_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate is less than or equal to SMALLER_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.lessThanOrEqual=" + SMALLER_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate is less than DEFAULT_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.lessThan=" + DEFAULT_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate is less than UPDATED_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.lessThan=" + UPDATED_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByLastActivityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where lastActivityDate is greater than DEFAULT_LAST_ACTIVITY_DATE
        defaultInventoryShouldNotBeFound("lastActivityDate.greaterThan=" + DEFAULT_LAST_ACTIVITY_DATE);

        // Get all the inventoryList where lastActivityDate is greater than SMALLER_LAST_ACTIVITY_DATE
        defaultInventoryShouldBeFound("lastActivityDate.greaterThan=" + SMALLER_LAST_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllInventoriesByShelfIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where shelf equals to DEFAULT_SHELF
        defaultInventoryShouldBeFound("shelf.equals=" + DEFAULT_SHELF);

        // Get all the inventoryList where shelf equals to UPDATED_SHELF
        defaultInventoryShouldNotBeFound("shelf.equals=" + UPDATED_SHELF);
    }

    @Test
    @Transactional
    void getAllInventoriesByShelfIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where shelf in DEFAULT_SHELF or UPDATED_SHELF
        defaultInventoryShouldBeFound("shelf.in=" + DEFAULT_SHELF + "," + UPDATED_SHELF);

        // Get all the inventoryList where shelf equals to UPDATED_SHELF
        defaultInventoryShouldNotBeFound("shelf.in=" + UPDATED_SHELF);
    }

    @Test
    @Transactional
    void getAllInventoriesByShelfIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where shelf is not null
        defaultInventoryShouldBeFound("shelf.specified=true");

        // Get all the inventoryList where shelf is null
        defaultInventoryShouldNotBeFound("shelf.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByShelfContainsSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where shelf contains DEFAULT_SHELF
        defaultInventoryShouldBeFound("shelf.contains=" + DEFAULT_SHELF);

        // Get all the inventoryList where shelf contains UPDATED_SHELF
        defaultInventoryShouldNotBeFound("shelf.contains=" + UPDATED_SHELF);
    }

    @Test
    @Transactional
    void getAllInventoriesByShelfNotContainsSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where shelf does not contain DEFAULT_SHELF
        defaultInventoryShouldNotBeFound("shelf.doesNotContain=" + DEFAULT_SHELF);

        // Get all the inventoryList where shelf does not contain UPDATED_SHELF
        defaultInventoryShouldBeFound("shelf.doesNotContain=" + UPDATED_SHELF);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint equals to DEFAULT_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.equals=" + DEFAULT_REORDER_POINT);

        // Get all the inventoryList where reorderPoint equals to UPDATED_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.equals=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint in DEFAULT_REORDER_POINT or UPDATED_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.in=" + DEFAULT_REORDER_POINT + "," + UPDATED_REORDER_POINT);

        // Get all the inventoryList where reorderPoint equals to UPDATED_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.in=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint is not null
        defaultInventoryShouldBeFound("reorderPoint.specified=true");

        // Get all the inventoryList where reorderPoint is null
        defaultInventoryShouldNotBeFound("reorderPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint is greater than or equal to DEFAULT_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.greaterThanOrEqual=" + DEFAULT_REORDER_POINT);

        // Get all the inventoryList where reorderPoint is greater than or equal to UPDATED_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.greaterThanOrEqual=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint is less than or equal to DEFAULT_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.lessThanOrEqual=" + DEFAULT_REORDER_POINT);

        // Get all the inventoryList where reorderPoint is less than or equal to SMALLER_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.lessThanOrEqual=" + SMALLER_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsLessThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint is less than DEFAULT_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.lessThan=" + DEFAULT_REORDER_POINT);

        // Get all the inventoryList where reorderPoint is less than UPDATED_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.lessThan=" + UPDATED_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByReorderPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where reorderPoint is greater than DEFAULT_REORDER_POINT
        defaultInventoryShouldNotBeFound("reorderPoint.greaterThan=" + DEFAULT_REORDER_POINT);

        // Get all the inventoryList where reorderPoint is greater than SMALLER_REORDER_POINT
        defaultInventoryShouldBeFound("reorderPoint.greaterThan=" + SMALLER_REORDER_POINT);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime equals to DEFAULT_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.equals=" + DEFAULT_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime equals to UPDATED_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.equals=" + UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime in DEFAULT_VENDOR_LEAD_TIME or UPDATED_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.in=" + DEFAULT_VENDOR_LEAD_TIME + "," + UPDATED_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime equals to UPDATED_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.in=" + UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime is not null
        defaultInventoryShouldBeFound("vendorLeadTime.specified=true");

        // Get all the inventoryList where vendorLeadTime is null
        defaultInventoryShouldNotBeFound("vendorLeadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime is greater than or equal to DEFAULT_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.greaterThanOrEqual=" + DEFAULT_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime is greater than or equal to UPDATED_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.greaterThanOrEqual=" + UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime is less than or equal to DEFAULT_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.lessThanOrEqual=" + DEFAULT_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime is less than or equal to SMALLER_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.lessThanOrEqual=" + SMALLER_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime is less than DEFAULT_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.lessThan=" + DEFAULT_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime is less than UPDATED_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.lessThan=" + UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByVendorLeadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList where vendorLeadTime is greater than DEFAULT_VENDOR_LEAD_TIME
        defaultInventoryShouldNotBeFound("vendorLeadTime.greaterThan=" + DEFAULT_VENDOR_LEAD_TIME);

        // Get all the inventoryList where vendorLeadTime is greater than SMALLER_VENDOR_LEAD_TIME
        defaultInventoryShouldBeFound("vendorLeadTime.greaterThan=" + SMALLER_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllInventoriesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            inventoryRepository.saveAndFlush(inventory);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        inventory.setLocation(location);
        inventoryRepository.saveAndFlush(inventory);
        Long locationId = location.getId();

        // Get all the inventoryList where location equals to locationId
        defaultInventoryShouldBeFound("locationId.equals=" + locationId);

        // Get all the inventoryList where location equals to (locationId + 1)
        defaultInventoryShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllInventoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inventoryRepository.saveAndFlush(inventory);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        inventory.setProduct(product);
        inventoryRepository.saveAndFlush(inventory);
        Long productId = product.getId();

        // Get all the inventoryList where product equals to productId
        defaultInventoryShouldBeFound("productId.equals=" + productId);

        // Get all the inventoryList where product equals to (productId + 1)
        defaultInventoryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryShouldBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].lastActivityDate").value(hasItem(DEFAULT_LAST_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].shelf").value(hasItem(DEFAULT_SHELF)))
            .andExpect(jsonPath("$.[*].reorderPoint").value(hasItem(sameNumber(DEFAULT_REORDER_POINT))))
            .andExpect(jsonPath("$.[*].vendorLeadTime").value(hasItem(DEFAULT_VENDOR_LEAD_TIME)));

        // Check, that the count call also returns 1
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryShouldNotBeFound(String filter) throws Exception {
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory
            .qty(UPDATED_QTY)
            .unitCost(UPDATED_UNIT_COST)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE)
            .shelf(UPDATED_SHELF)
            .reorderPoint(UPDATED_REORDER_POINT)
            .vendorLeadTime(UPDATED_VENDOR_LEAD_TIME);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(updatedInventory);

        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testInventory.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testInventory.getLastActivityDate()).isEqualTo(UPDATED_LAST_ACTIVITY_DATE);
        assertThat(testInventory.getShelf()).isEqualTo(UPDATED_SHELF);
        assertThat(testInventory.getReorderPoint()).isEqualByComparingTo(UPDATED_REORDER_POINT);
        assertThat(testInventory.getVendorLeadTime()).isEqualTo(UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void putNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.unitCost(UPDATED_UNIT_COST).lastActivityDate(UPDATED_LAST_ACTIVITY_DATE);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQty()).isEqualByComparingTo(DEFAULT_QTY);
        assertThat(testInventory.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testInventory.getLastActivityDate()).isEqualTo(UPDATED_LAST_ACTIVITY_DATE);
        assertThat(testInventory.getShelf()).isEqualTo(DEFAULT_SHELF);
        assertThat(testInventory.getReorderPoint()).isEqualByComparingTo(DEFAULT_REORDER_POINT);
        assertThat(testInventory.getVendorLeadTime()).isEqualTo(DEFAULT_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory
            .qty(UPDATED_QTY)
            .unitCost(UPDATED_UNIT_COST)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE)
            .shelf(UPDATED_SHELF)
            .reorderPoint(UPDATED_REORDER_POINT)
            .vendorLeadTime(UPDATED_VENDOR_LEAD_TIME);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testInventory.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testInventory.getLastActivityDate()).isEqualTo(UPDATED_LAST_ACTIVITY_DATE);
        assertThat(testInventory.getShelf()).isEqualTo(UPDATED_SHELF);
        assertThat(testInventory.getReorderPoint()).isEqualByComparingTo(UPDATED_REORDER_POINT);
        assertThat(testInventory.getVendorLeadTime()).isEqualTo(UPDATED_VENDOR_LEAD_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(count.incrementAndGet());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Delete the inventory
        restInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
