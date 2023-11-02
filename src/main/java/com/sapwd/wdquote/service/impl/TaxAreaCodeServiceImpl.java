package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.TaxAreaCode;
import com.sapwd.wdquote.repository.TaxAreaCodeRepository;
import com.sapwd.wdquote.service.TaxAreaCodeService;
import com.sapwd.wdquote.service.dto.TaxAreaCodeDTO;
import com.sapwd.wdquote.service.mapper.TaxAreaCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaxAreaCode}.
 */
@Service
@Transactional
public class TaxAreaCodeServiceImpl implements TaxAreaCodeService {

    private final Logger log = LoggerFactory.getLogger(TaxAreaCodeServiceImpl.class);

    private final TaxAreaCodeRepository taxAreaCodeRepository;

    private final TaxAreaCodeMapper taxAreaCodeMapper;

    public TaxAreaCodeServiceImpl(TaxAreaCodeRepository taxAreaCodeRepository, TaxAreaCodeMapper taxAreaCodeMapper) {
        this.taxAreaCodeRepository = taxAreaCodeRepository;
        this.taxAreaCodeMapper = taxAreaCodeMapper;
    }

    @Override
    public TaxAreaCodeDTO save(TaxAreaCodeDTO taxAreaCodeDTO) {
        log.debug("Request to save TaxAreaCode : {}", taxAreaCodeDTO);
        TaxAreaCode taxAreaCode = taxAreaCodeMapper.toEntity(taxAreaCodeDTO);
        taxAreaCode = taxAreaCodeRepository.save(taxAreaCode);
        return taxAreaCodeMapper.toDto(taxAreaCode);
    }

    @Override
    public TaxAreaCodeDTO update(TaxAreaCodeDTO taxAreaCodeDTO) {
        log.debug("Request to update TaxAreaCode : {}", taxAreaCodeDTO);
        TaxAreaCode taxAreaCode = taxAreaCodeMapper.toEntity(taxAreaCodeDTO);
        taxAreaCode = taxAreaCodeRepository.save(taxAreaCode);
        return taxAreaCodeMapper.toDto(taxAreaCode);
    }

    @Override
    public Optional<TaxAreaCodeDTO> partialUpdate(TaxAreaCodeDTO taxAreaCodeDTO) {
        log.debug("Request to partially update TaxAreaCode : {}", taxAreaCodeDTO);

        return taxAreaCodeRepository
            .findById(taxAreaCodeDTO.getId())
            .map(existingTaxAreaCode -> {
                taxAreaCodeMapper.partialUpdate(existingTaxAreaCode, taxAreaCodeDTO);

                return existingTaxAreaCode;
            })
            .map(taxAreaCodeRepository::save)
            .map(taxAreaCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxAreaCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxAreaCodes");
        return taxAreaCodeRepository.findAllByDeletedIsFalse(pageable).map(taxAreaCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxAreaCodeDTO> findOne(Long id) {
        log.debug("Request to get TaxAreaCode : {}", id);
        return taxAreaCodeRepository.findById(id).map(taxAreaCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxAreaCode : {}", id);
        Optional<TaxAreaCode> taxAreaCodeOptional = taxAreaCodeRepository.findById(id);
        if (taxAreaCodeOptional.isPresent()) {
            TaxAreaCode taxAreaCode = taxAreaCodeOptional.orElseThrow();
            taxAreaCode.setDeleted(true);
            taxAreaCodeRepository.save(taxAreaCode);
        }
    }
}
