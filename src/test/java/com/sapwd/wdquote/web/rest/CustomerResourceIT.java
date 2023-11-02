package com.sapwd.wdquote.web.rest;

import static com.sapwd.wdquote.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Customer;
import com.sapwd.wdquote.domain.DeliveryZone;
import com.sapwd.wdquote.domain.TaxAreaCode;
import com.sapwd.wdquote.repository.CustomerRepository;
import com.sapwd.wdquote.service.dto.CustomerDTO;
import com.sapwd.wdquote.service.mapper.CustomerMapper;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TAX_LIABLE = false;
    private static final Boolean UPDATED_TAX_LIABLE = true;

    private static final String DEFAULT_TAX_EXEMPTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_EXEMPTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TERMS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_LIMIT = new BigDecimal(2);

    private static final Boolean DEFAULT_BLOCKED = false;
    private static final Boolean UPDATED_BLOCKED = true;

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .taxLiable(DEFAULT_TAX_LIABLE)
            .taxExemptionCode(DEFAULT_TAX_EXEMPTION_CODE)
            .paymentTerms(DEFAULT_PAYMENT_TERMS)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .blocked(DEFAULT_BLOCKED);
        // Add required entity
        DeliveryZone deliveryZone;
        if (TestUtil.findAll(em, DeliveryZone.class).isEmpty()) {
            deliveryZone = DeliveryZoneResourceIT.createEntity(em);
            em.persist(deliveryZone);
            em.flush();
        } else {
            deliveryZone = TestUtil.findAll(em, DeliveryZone.class).get(0);
        }
        customer.setDeliveryZone(deliveryZone);
        // Add required entity
        TaxAreaCode taxAreaCode;
        if (TestUtil.findAll(em, TaxAreaCode.class).isEmpty()) {
            taxAreaCode = TaxAreaCodeResourceIT.createEntity(em);
            em.persist(taxAreaCode);
            em.flush();
        } else {
            taxAreaCode = TestUtil.findAll(em, TaxAreaCode.class).get(0);
        }
        customer.setTaxAreaCode(taxAreaCode);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .taxLiable(UPDATED_TAX_LIABLE)
            .taxExemptionCode(UPDATED_TAX_EXEMPTION_CODE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .blocked(UPDATED_BLOCKED);
        // Add required entity
        DeliveryZone deliveryZone;
        if (TestUtil.findAll(em, DeliveryZone.class).isEmpty()) {
            deliveryZone = DeliveryZoneResourceIT.createUpdatedEntity(em);
            em.persist(deliveryZone);
            em.flush();
        } else {
            deliveryZone = TestUtil.findAll(em, DeliveryZone.class).get(0);
        }
        customer.setDeliveryZone(deliveryZone);
        // Add required entity
        TaxAreaCode taxAreaCode;
        if (TestUtil.findAll(em, TaxAreaCode.class).isEmpty()) {
            taxAreaCode = TaxAreaCodeResourceIT.createUpdatedEntity(em);
            em.persist(taxAreaCode);
            em.flush();
        } else {
            taxAreaCode = TestUtil.findAll(em, TaxAreaCode.class).get(0);
        }
        customer.setTaxAreaCode(taxAreaCode);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomer.getTaxLiable()).isEqualTo(DEFAULT_TAX_LIABLE);
        assertThat(testCustomer.getTaxExemptionCode()).isEqualTo(DEFAULT_TAX_EXEMPTION_CODE);
        assertThat(testCustomer.getPaymentTerms()).isEqualTo(DEFAULT_PAYMENT_TERMS);
        assertThat(testCustomer.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testCustomer.getBlocked()).isEqualTo(DEFAULT_BLOCKED);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCode(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setName(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxLiableIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setTaxLiable(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBlockedIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setBlocked(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].taxLiable").value(hasItem(DEFAULT_TAX_LIABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].taxExemptionCode").value(hasItem(DEFAULT_TAX_EXEMPTION_CODE)))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED.booleanValue())));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.taxLiable").value(DEFAULT_TAX_LIABLE.booleanValue()))
            .andExpect(jsonPath("$.taxExemptionCode").value(DEFAULT_TAX_EXEMPTION_CODE))
            .andExpect(jsonPath("$.paymentTerms").value(DEFAULT_PAYMENT_TERMS))
            .andExpect(jsonPath("$.creditLimit").value(sameNumber(DEFAULT_CREDIT_LIMIT)))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .taxLiable(UPDATED_TAX_LIABLE)
            .taxExemptionCode(UPDATED_TAX_EXEMPTION_CODE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .blocked(UPDATED_BLOCKED);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTaxLiable()).isEqualTo(UPDATED_TAX_LIABLE);
        assertThat(testCustomer.getTaxExemptionCode()).isEqualTo(UPDATED_TAX_EXEMPTION_CODE);
        assertThat(testCustomer.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testCustomer.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testCustomer.getBlocked()).isEqualTo(UPDATED_BLOCKED);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.code(UPDATED_CODE).taxExemptionCode(UPDATED_TAX_EXEMPTION_CODE).blocked(UPDATED_BLOCKED);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomer.getTaxLiable()).isEqualTo(DEFAULT_TAX_LIABLE);
        assertThat(testCustomer.getTaxExemptionCode()).isEqualTo(UPDATED_TAX_EXEMPTION_CODE);
        assertThat(testCustomer.getPaymentTerms()).isEqualTo(DEFAULT_PAYMENT_TERMS);
        assertThat(testCustomer.getCreditLimit()).isEqualByComparingTo(DEFAULT_CREDIT_LIMIT);
        assertThat(testCustomer.getBlocked()).isEqualTo(UPDATED_BLOCKED);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .taxLiable(UPDATED_TAX_LIABLE)
            .taxExemptionCode(UPDATED_TAX_EXEMPTION_CODE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .blocked(UPDATED_BLOCKED);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTaxLiable()).isEqualTo(UPDATED_TAX_LIABLE);
        assertThat(testCustomer.getTaxExemptionCode()).isEqualTo(UPDATED_TAX_EXEMPTION_CODE);
        assertThat(testCustomer.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testCustomer.getCreditLimit()).isEqualByComparingTo(UPDATED_CREDIT_LIMIT);
        assertThat(testCustomer.getBlocked()).isEqualTo(UPDATED_BLOCKED);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
