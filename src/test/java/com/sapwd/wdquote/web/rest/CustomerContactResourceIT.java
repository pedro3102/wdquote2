package com.sapwd.wdquote.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sapwd.wdquote.IntegrationTest;
import com.sapwd.wdquote.domain.Customer;
import com.sapwd.wdquote.domain.CustomerContact;
import com.sapwd.wdquote.domain.Language;
import com.sapwd.wdquote.repository.CustomerContactRepository;
import com.sapwd.wdquote.service.dto.CustomerContactDTO;
import com.sapwd.wdquote.service.mapper.CustomerContactMapper;
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
 * Integration tests for the {@link CustomerContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerContactResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "-+d7h@q.nvbnnki";
    private static final String UPDATED_EMAIL = "8w_c4@i.hk.luyo";

    private static final String DEFAULT_SALESPERSON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SALESPERSON_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT_CONTACT = false;
    private static final Boolean UPDATED_IS_DEFAULT_CONTACT = true;

    private static final String ENTITY_API_URL = "/api/customer-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerContactRepository customerContactRepository;

    @Autowired
    private CustomerContactMapper customerContactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerContactMockMvc;

    private CustomerContact customerContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerContact createEntity(EntityManager em) {
        CustomerContact customerContact = new CustomerContact()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .salespersonCode(DEFAULT_SALESPERSON_CODE)
            .isDefaultContact(DEFAULT_IS_DEFAULT_CONTACT);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        customerContact.setCustomer(customer);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        customerContact.setLanguage(language);
        return customerContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerContact createUpdatedEntity(EntityManager em) {
        CustomerContact customerContact = new CustomerContact()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .salespersonCode(UPDATED_SALESPERSON_CODE)
            .isDefaultContact(UPDATED_IS_DEFAULT_CONTACT);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        customerContact.setCustomer(customer);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createUpdatedEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        customerContact.setLanguage(language);
        return customerContact;
    }

    @BeforeEach
    public void initTest() {
        customerContact = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerContact() throws Exception {
        int databaseSizeBeforeCreate = customerContactRepository.findAll().size();
        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);
        restCustomerContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerContact testCustomerContact = customerContactList.get(customerContactList.size() - 1);
        assertThat(testCustomerContact.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCustomerContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerContact.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomerContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerContact.getSalespersonCode()).isEqualTo(DEFAULT_SALESPERSON_CODE);
        assertThat(testCustomerContact.getIsDefaultContact()).isEqualTo(DEFAULT_IS_DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void createCustomerContactWithExistingId() throws Exception {
        // Create the CustomerContact with an existing ID
        customerContact.setId(1L);
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        int databaseSizeBeforeCreate = customerContactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerContactRepository.findAll().size();
        // set the field null
        customerContact.setCode(null);

        // Create the CustomerContact, which fails.
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        restCustomerContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerContactRepository.findAll().size();
        // set the field null
        customerContact.setName(null);

        // Create the CustomerContact, which fails.
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        restCustomerContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDefaultContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerContactRepository.findAll().size();
        // set the field null
        customerContact.setIsDefaultContact(null);

        // Create the CustomerContact, which fails.
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        restCustomerContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerContacts() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        // Get all the customerContactList
        restCustomerContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].salespersonCode").value(hasItem(DEFAULT_SALESPERSON_CODE)))
            .andExpect(jsonPath("$.[*].isDefaultContact").value(hasItem(DEFAULT_IS_DEFAULT_CONTACT.booleanValue())));
    }

    @Test
    @Transactional
    void getCustomerContact() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        // Get the customerContact
        restCustomerContactMockMvc
            .perform(get(ENTITY_API_URL_ID, customerContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerContact.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.salespersonCode").value(DEFAULT_SALESPERSON_CODE))
            .andExpect(jsonPath("$.isDefaultContact").value(DEFAULT_IS_DEFAULT_CONTACT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCustomerContact() throws Exception {
        // Get the customerContact
        restCustomerContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerContact() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();

        // Update the customerContact
        CustomerContact updatedCustomerContact = customerContactRepository.findById(customerContact.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerContact are not directly saved in db
        em.detach(updatedCustomerContact);
        updatedCustomerContact
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .salespersonCode(UPDATED_SALESPERSON_CODE)
            .isDefaultContact(UPDATED_IS_DEFAULT_CONTACT);
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(updatedCustomerContact);

        restCustomerContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
        CustomerContact testCustomerContact = customerContactList.get(customerContactList.size() - 1);
        assertThat(testCustomerContact.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomerContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomerContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerContact.getSalespersonCode()).isEqualTo(UPDATED_SALESPERSON_CODE);
        assertThat(testCustomerContact.getIsDefaultContact()).isEqualTo(UPDATED_IS_DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void putNonExistingCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerContactWithPatch() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();

        // Update the customerContact using partial update
        CustomerContact partialUpdatedCustomerContact = new CustomerContact();
        partialUpdatedCustomerContact.setId(customerContact.getId());

        partialUpdatedCustomerContact.salespersonCode(UPDATED_SALESPERSON_CODE);

        restCustomerContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerContact))
            )
            .andExpect(status().isOk());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
        CustomerContact testCustomerContact = customerContactList.get(customerContactList.size() - 1);
        assertThat(testCustomerContact.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCustomerContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerContact.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomerContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerContact.getSalespersonCode()).isEqualTo(UPDATED_SALESPERSON_CODE);
        assertThat(testCustomerContact.getIsDefaultContact()).isEqualTo(DEFAULT_IS_DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateCustomerContactWithPatch() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();

        // Update the customerContact using partial update
        CustomerContact partialUpdatedCustomerContact = new CustomerContact();
        partialUpdatedCustomerContact.setId(customerContact.getId());

        partialUpdatedCustomerContact
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .salespersonCode(UPDATED_SALESPERSON_CODE)
            .isDefaultContact(UPDATED_IS_DEFAULT_CONTACT);

        restCustomerContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerContact))
            )
            .andExpect(status().isOk());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
        CustomerContact testCustomerContact = customerContactList.get(customerContactList.size() - 1);
        assertThat(testCustomerContact.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomerContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomerContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerContact.getSalespersonCode()).isEqualTo(UPDATED_SALESPERSON_CODE);
        assertThat(testCustomerContact.getIsDefaultContact()).isEqualTo(UPDATED_IS_DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerContact() throws Exception {
        int databaseSizeBeforeUpdate = customerContactRepository.findAll().size();
        customerContact.setId(count.incrementAndGet());

        // Create the CustomerContact
        CustomerContactDTO customerContactDTO = customerContactMapper.toDto(customerContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerContactMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerContact in the database
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerContact() throws Exception {
        // Initialize the database
        customerContactRepository.saveAndFlush(customerContact);

        int databaseSizeBeforeDelete = customerContactRepository.findAll().size();

        // Delete the customerContact
        restCustomerContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerContact> customerContactList = customerContactRepository.findAll();
        assertThat(customerContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
