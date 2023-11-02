package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.UnitOfMeasure;
import com.sapwd.wdquote.repository.UnitOfMeasureRepository;
import com.sapwd.wdquote.service.UnitOfMeasureConversionService;
import com.sapwd.wdquote.service.UnitOfMeasureService;
import com.sapwd.wdquote.service.dto.UnitOfMeasureDTO;
import com.sapwd.wdquote.service.mapper.UnitOfMeasureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitOfMeasure}.
 */
@Service
@Transactional
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureServiceImpl.class);

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UnitOfMeasureMapper unitOfMeasureMapper;

    private final UnitOfMeasureConversionService unitOfMeasureConversionService;

    public UnitOfMeasureServiceImpl(
        UnitOfMeasureRepository unitOfMeasureRepository,
        UnitOfMeasureMapper unitOfMeasureMapper,
        UnitOfMeasureConversionService unitOfMeasureConversionService
    ) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureMapper = unitOfMeasureMapper;
        this.unitOfMeasureConversionService = unitOfMeasureConversionService;
    }

    @Override
    public UnitOfMeasureDTO save(UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("Request to save UnitOfMeasure : {}", unitOfMeasureDTO);
        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(unitOfMeasureDTO);
        unitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);
        return unitOfMeasureMapper.toDto(unitOfMeasure);
    }

    @Override
    public UnitOfMeasureDTO update(UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("Request to update UnitOfMeasure : {}", unitOfMeasureDTO);
        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(unitOfMeasureDTO);
        unitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);
        return unitOfMeasureMapper.toDto(unitOfMeasure);
    }

    @Override
    public Optional<UnitOfMeasureDTO> partialUpdate(UnitOfMeasureDTO unitOfMeasureDTO) {
        log.debug("Request to partially update UnitOfMeasure : {}", unitOfMeasureDTO);

        return unitOfMeasureRepository
            .findById(unitOfMeasureDTO.getId())
            .map(existingUnitOfMeasure -> {
                unitOfMeasureMapper.partialUpdate(existingUnitOfMeasure, unitOfMeasureDTO);

                return existingUnitOfMeasure;
            })
            .map(unitOfMeasureRepository::save)
            .map(unitOfMeasureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitOfMeasureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UnitOfMeasures");
        return unitOfMeasureRepository.findAllByDeletedIsFalse(pageable).map(unitOfMeasureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasureDTO> findOne(Long id) {
        log.debug("Request to get UnitOfMeasure : {}", id);
        return unitOfMeasureRepository.findById(id).map(unitOfMeasureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasure : {}", id);
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findById(id);
        if (unitOfMeasureOptional.isPresent()) {
            unitOfMeasureConversionService
                .findAllByUnitOfMeasureId(unitOfMeasureOptional.get().getId())
                .forEach(uomC -> {
                    unitOfMeasureConversionService.delete(uomC.getId());
                });
            UnitOfMeasure unitOfMeasure = unitOfMeasureOptional.orElseThrow();
            unitOfMeasure.setDeleted(true);
            unitOfMeasureRepository.save(unitOfMeasure);
        }
    }
}
