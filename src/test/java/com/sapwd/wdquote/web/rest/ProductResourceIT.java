package com.sapwd.wdquote.web.rest;

import static com.sapwd.wdquote.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Product;
import com.sapwd.wdquote.domain.ProductCategory;
import com.sapwd.wdquote.domain.SystemModel;
import com.sapwd.wdquote.domain.UnitOfMeasure;
import com.sapwd.wdquote.repository.ProductRepository;
import com.sapwd.wdquote.service.ProductService;
import com.sapwd.wdquote.service.dto.ProductDTO;
import com.sapwd.wdquote.service.mapper.ProductMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WEIGHT = new BigDecimal(1 - 1);

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).weight(DEFAULT_WEIGHT).picture(DEFAULT_PICTURE);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        product.setUom(unitOfMeasure);
        // Add required entity
        product.setUomWeight(unitOfMeasure);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createEntity(em);
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).get(0);
        }
        product.setCategory(productCategory);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT).picture(UPDATED_PICTURE);
        // Add required entity
        UnitOfMeasure unitOfMeasure;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            unitOfMeasure = UnitOfMeasureResourceIT.createUpdatedEntity(em);
            em.persist(unitOfMeasure);
            em.flush();
        } else {
            unitOfMeasure = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        product.setUom(unitOfMeasure);
        // Add required entity
        product.setUomWeight(unitOfMeasure);
        // Add required entity
        ProductCategory productCategory;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productCategory = ProductCategoryResourceIT.createUpdatedEntity(em);
            em.persist(productCategory);
            em.flush();
        } else {
            productCategory = TestUtil.findAll(em, ProductCategory.class).get(0);
        }
        product.setCategory(productCategory);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getWeight()).isEqualByComparingTo(DEFAULT_WEIGHT);
        assertThat(testProduct.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setCode(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setDescription(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(sameNumber(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where code equals to DEFAULT_CODE
        defaultProductShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the productList where code equals to UPDATED_CODE
        defaultProductShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProductShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the productList where code equals to UPDATED_CODE
        defaultProductShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where code is not null
        defaultProductShouldBeFound("code.specified=true");

        // Get all the productList where code is null
        defaultProductShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCodeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where code contains DEFAULT_CODE
        defaultProductShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the productList where code contains UPDATED_CODE
        defaultProductShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where code does not contain DEFAULT_CODE
        defaultProductShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the productList where code does not contain UPDATED_CODE
        defaultProductShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight equals to DEFAULT_WEIGHT
        defaultProductShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the productList where weight equals to UPDATED_WEIGHT
        defaultProductShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultProductShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the productList where weight equals to UPDATED_WEIGHT
        defaultProductShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight is not null
        defaultProductShouldBeFound("weight.specified=true");

        // Get all the productList where weight is null
        defaultProductShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultProductShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the productList where weight is greater than or equal to UPDATED_WEIGHT
        defaultProductShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than or equal to DEFAULT_WEIGHT
        defaultProductShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the productList where weight is less than or equal to SMALLER_WEIGHT
        defaultProductShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than DEFAULT_WEIGHT
        defaultProductShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the productList where weight is less than UPDATED_WEIGHT
        defaultProductShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than DEFAULT_WEIGHT
        defaultProductShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the productList where weight is greater than SMALLER_WEIGHT
        defaultProductShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByPictureIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where picture equals to DEFAULT_PICTURE
        defaultProductShouldBeFound("picture.equals=" + DEFAULT_PICTURE);

        // Get all the productList where picture equals to UPDATED_PICTURE
        defaultProductShouldNotBeFound("picture.equals=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllProductsByPictureIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where picture in DEFAULT_PICTURE or UPDATED_PICTURE
        defaultProductShouldBeFound("picture.in=" + DEFAULT_PICTURE + "," + UPDATED_PICTURE);

        // Get all the productList where picture equals to UPDATED_PICTURE
        defaultProductShouldNotBeFound("picture.in=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllProductsByPictureIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where picture is not null
        defaultProductShouldBeFound("picture.specified=true");

        // Get all the productList where picture is null
        defaultProductShouldNotBeFound("picture.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPictureContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where picture contains DEFAULT_PICTURE
        defaultProductShouldBeFound("picture.contains=" + DEFAULT_PICTURE);

        // Get all the productList where picture contains UPDATED_PICTURE
        defaultProductShouldNotBeFound("picture.contains=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllProductsByPictureNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where picture does not contain DEFAULT_PICTURE
        defaultProductShouldNotBeFound("picture.doesNotContain=" + DEFAULT_PICTURE);

        // Get all the productList where picture does not contain UPDATED_PICTURE
        defaultProductShouldBeFound("picture.doesNotContain=" + UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void getAllProductsByUomIsEqualToSomething() throws Exception {
        UnitOfMeasure uom;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            uom = UnitOfMeasureResourceIT.createEntity(em);
        } else {
            uom = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        em.persist(uom);
        em.flush();
        product.setUom(uom);
        productRepository.saveAndFlush(product);
        Long uomId = uom.getId();

        // Get all the productList where uom equals to uomId
        defaultProductShouldBeFound("uomId.equals=" + uomId);

        // Get all the productList where uom equals to (uomId + 1)
        defaultProductShouldNotBeFound("uomId.equals=" + (uomId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByUomWeightIsEqualToSomething() throws Exception {
        UnitOfMeasure uomWeight;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            uomWeight = UnitOfMeasureResourceIT.createEntity(em);
        } else {
            uomWeight = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        em.persist(uomWeight);
        em.flush();
        product.setUomWeight(uomWeight);
        productRepository.saveAndFlush(product);
        Long uomWeightId = uomWeight.getId();

        // Get all the productList where uomWeight equals to uomWeightId
        defaultProductShouldBeFound("uomWeightId.equals=" + uomWeightId);

        // Get all the productList where uomWeight equals to (uomWeightId + 1)
        defaultProductShouldNotBeFound("uomWeightId.equals=" + (uomWeightId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        ProductCategory category;
        if (TestUtil.findAll(em, ProductCategory.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            category = ProductCategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, ProductCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();

        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to (categoryId + 1)
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySystemModelIsEqualToSomething() throws Exception {
        SystemModel systemModel;
        if (TestUtil.findAll(em, SystemModel.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            systemModel = SystemModelResourceIT.createEntity(em);
        } else {
            systemModel = TestUtil.findAll(em, SystemModel.class).get(0);
        }
        em.persist(systemModel);
        em.flush();
        product.addSystemModel(systemModel);
        productRepository.saveAndFlush(product);
        Long systemModelId = systemModel.getId();

        // Get all the productList where systemModel equals to systemModelId
        defaultProductShouldBeFound("systemModelId.equals=" + systemModelId);

        // Get all the productList where systemModel equals to (systemModelId + 1)
        defaultProductShouldNotBeFound("systemModelId.equals=" + (systemModelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT).picture(UPDATED_PICTURE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getWeight()).isEqualByComparingTo(UPDATED_WEIGHT);
        assertThat(testProduct.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getWeight()).isEqualByComparingTo(DEFAULT_WEIGHT);
        assertThat(testProduct.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).weight(UPDATED_WEIGHT).picture(UPDATED_PICTURE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getWeight()).isEqualByComparingTo(UPDATED_WEIGHT);
        assertThat(testProduct.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
