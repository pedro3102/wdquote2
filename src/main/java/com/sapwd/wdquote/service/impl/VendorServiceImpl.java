package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.Vendor;
import com.sapwd.wdquote.repository.VendorRepository;
import com.sapwd.wdquote.service.VendorService;
import com.sapwd.wdquote.service.dto.VendorDTO;
import com.sapwd.wdquote.service.mapper.VendorMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendor}.
 */
@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    private final Logger log = LoggerFactory.getLogger(VendorServiceImpl.class);

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorDTO save(VendorDTO vendorDTO) {
        log.debug("Request to save Vendor : {}", vendorDTO);
        Vendor vendor = vendorMapper.toEntity(vendorDTO);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    public VendorDTO update(VendorDTO vendorDTO) {
        log.debug("Request to update Vendor : {}", vendorDTO);
        Vendor vendor = vendorMapper.toEntity(vendorDTO);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    public Optional<VendorDTO> partialUpdate(VendorDTO vendorDTO) {
        log.debug("Request to partially update Vendor : {}", vendorDTO);

        return vendorRepository
            .findById(vendorDTO.getId())
            .map(existingVendor -> {
                vendorMapper.partialUpdate(existingVendor, vendorDTO);

                return existingVendor;
            })
            .map(vendorRepository::save)
            .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vendors");
        return vendorRepository.findAll(pageable).map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VendorDTO> findOne(Long id) {
        log.debug("Request to get Vendor : {}", id);
        return vendorRepository.findById(id).map(vendorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vendor : {}", id);
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()) {
            Vendor vendor = vendorOptional.orElseThrow();
            vendor.setDeleted(true);
            vendorRepository.save(vendor);
        }
    }

    @Override
    public List<VendorDTO> findAllBasic() {
        log.debug("Request to get all Customers with basic info");
        return vendorRepository.findAllByDeletedIsFalse().stream().map(vendorMapper::toDtoBasic).collect(Collectors.toList());
    }
}
