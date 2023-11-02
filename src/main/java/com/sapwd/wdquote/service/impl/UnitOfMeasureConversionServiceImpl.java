package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.UnitOfMeasureConversion;
import com.sapwd.wdquote.repository.UnitOfMeasureConversionRepository;
import com.sapwd.wdquote.service.UnitOfMeasureConversionService;
import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
import com.sapwd.wdquote.service.mapper.UnitOfMeasureConversionMapper;
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
 * Service Implementation for managing {@link UnitOfMeasureConversion}.
 */
@Service
@Transactional
public class UnitOfMeasureConversionServiceImpl implements UnitOfMeasureConversionService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureConversionServiceImpl.class);

    private final UnitOfMeasureConversionRepository unitOfMeasureConversionRepository;

    private final UnitOfMeasureConversionMapper unitOfMeasureConversionMapper;

    public UnitOfMeasureConversionServiceImpl(
        UnitOfMeasureConversionRepository unitOfMeasureConversionRepository,
        UnitOfMeasureConversionMapper unitOfMeasureConversionMapper
    ) {
        this.unitOfMeasureConversionRepository = unitOfMeasureConversionRepository;
        this.unitOfMeasureConversionMapper = unitOfMeasureConversionMapper;
    }

    @Override
    public UnitOfMeasureConversionDTO save(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO) {
        log.debug("Request to save UnitOfMeasureConversion : {}", unitOfMeasureConversionDTO);
        UnitOfMeasureConversion unitOfMeasureConversion = unitOfMeasureConversionMapper.toEntity(unitOfMeasureConversionDTO);
        unitOfMeasureConversion = unitOfMeasureConversionRepository.save(unitOfMeasureConversion);
        return unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);
    }

    @Override
    public UnitOfMeasureConversionDTO update(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO) {
        log.debug("Request to update UnitOfMeasureConversion : {}", unitOfMeasureConversionDTO);
        UnitOfMeasureConversion unitOfMeasureConversion = unitOfMeasureConversionMapper.toEntity(unitOfMeasureConversionDTO);
        unitOfMeasureConversion = unitOfMeasureConversionRepository.save(unitOfMeasureConversion);
        return unitOfMeasureConversionMapper.toDto(unitOfMeasureConversion);
    }

    @Override
    public Optional<UnitOfMeasureConversionDTO> partialUpdate(UnitOfMeasureConversionDTO unitOfMeasureConversionDTO) {
        log.debug("Request to partially update UnitOfMeasureConversion : {}", unitOfMeasureConversionDTO);

        return unitOfMeasureConversionRepository
            .findById(unitOfMeasureConversionDTO.getId())
            .map(existingUnitOfMeasureConversion -> {
                unitOfMeasureConversionMapper.partialUpdate(existingUnitOfMeasureConversion, unitOfMeasureConversionDTO);

                return existingUnitOfMeasureConversion;
            })
            .map(unitOfMeasureConversionRepository::save)
            .map(unitOfMeasureConversionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitOfMeasureConversionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UnitOfMeasureConversions");
        return unitOfMeasureConversionRepository.findAll(pageable).map(unitOfMeasureConversionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitOfMeasureConversionDTO> findAllByUnitOfMeasureId(Long id) {
        log.debug("Request to get all UnitOfMeasureConversions by UnitOfMeasure: {}", id);
        return unitOfMeasureConversionRepository
            .findAllByUnitOfMeasureId(id)
            .stream()
            .map(unitOfMeasureConversionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasureConversionDTO> findOne(Long id) {
        log.debug("Request to get UnitOfMeasureConversion : {}", id);
        return unitOfMeasureConversionRepository.findById(id).map(unitOfMeasureConversionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasureConversion : {}", id);
        Optional<UnitOfMeasureConversion> unitOfMeasureConversionOptional = unitOfMeasureConversionRepository.findById(id);
        if (unitOfMeasureConversionOptional.isPresent()) {
            UnitOfMeasureConversion unitOfMeasureConversion = unitOfMeasureConversionOptional.orElseThrow();
            unitOfMeasureConversion.setDeleted(true);
            unitOfMeasureConversionRepository.save(unitOfMeasureConversion);
        }
    }
}
