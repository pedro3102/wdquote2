package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.CustomerContact;
import com.sapwd.wdquote.repository.CustomerContactRepository;
import com.sapwd.wdquote.service.CustomerContactService;
import com.sapwd.wdquote.service.dto.CustomerContactDTO;
import com.sapwd.wdquote.service.mapper.CustomerContactMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerContact}.
 */
@Service
@Transactional
public class CustomerContactServiceImpl implements CustomerContactService {

    private final Logger log = LoggerFactory.getLogger(CustomerContactServiceImpl.class);

    private final CustomerContactRepository customerContactRepository;

    private final CustomerContactMapper customerContactMapper;

    public CustomerContactServiceImpl(CustomerContactRepository customerContactRepository, CustomerContactMapper customerContactMapper) {
        this.customerContactRepository = customerContactRepository;
        this.customerContactMapper = customerContactMapper;
    }

    @Override
    public CustomerContactDTO save(CustomerContactDTO customerContactDTO) {
        log.debug("Request to save CustomerContact : {}", customerContactDTO);
        CustomerContact customerContact = customerContactMapper.toEntity(customerContactDTO);
        customerContact = customerContactRepository.save(customerContact);
        return customerContactMapper.toDto(customerContact);
    }

    @Override
    public CustomerContactDTO update(CustomerContactDTO customerContactDTO) {
        log.debug("Request to update CustomerContact : {}", customerContactDTO);
        CustomerContact customerContact = customerContactMapper.toEntity(customerContactDTO);
        customerContact = customerContactRepository.save(customerContact);
        return customerContactMapper.toDto(customerContact);
    }

    @Override
    public Optional<CustomerContactDTO> partialUpdate(CustomerContactDTO customerContactDTO) {
        log.debug("Request to partially update CustomerContact : {}", customerContactDTO);

        return customerContactRepository
            .findById(customerContactDTO.getId())
            .map(existingCustomerContact -> {
                customerContactMapper.partialUpdate(existingCustomerContact, customerContactDTO);

                return existingCustomerContact;
            })
            .map(customerContactRepository::save)
            .map(customerContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerContacts");
        return customerContactRepository.findAll(pageable).map(customerContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerContactDTO> findOne(Long id) {
        log.debug("Request to get CustomerContact : {}", id);
        return customerContactRepository.findById(id).map(customerContactMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerContact : {}", id);
        Optional<CustomerContact> customerContactOptional = customerContactRepository.findById(id);
        if (customerContactOptional.isPresent()) {
            CustomerContact customerContact = customerContactOptional.orElseThrow();
            customerContact.setDeleted(true);
            customerContactRepository.save(customerContact);
        }
    }
}
