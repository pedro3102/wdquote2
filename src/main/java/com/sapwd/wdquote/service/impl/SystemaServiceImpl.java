package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.Systema;
import com.sapwd.wdquote.repository.SystemaRepository;
import com.sapwd.wdquote.service.SystemaService;
import com.sapwd.wdquote.service.dto.SystemaDTO;
import com.sapwd.wdquote.service.mapper.SystemModelMapper;
import com.sapwd.wdquote.service.mapper.SystemaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Systema}.
 */
@Service
@Transactional
public class SystemaServiceImpl implements SystemaService {

    private final Logger log = LoggerFactory.getLogger(SystemaServiceImpl.class);

    private final SystemaRepository systemaRepository;

    private final SystemaMapper systemaMapper;

    private final SystemModelMapper systemModelMapper;

    public SystemaServiceImpl(SystemaRepository systemaRepository, SystemaMapper systemaMapper, SystemModelMapper systemModelMapper) {
        this.systemaRepository = systemaRepository;
        this.systemaMapper = systemaMapper;
        this.systemModelMapper = systemModelMapper;
    }

    @Override
    public SystemaDTO save(SystemaDTO systemaDTO) {
        log.debug("Request to save System : {}", systemaDTO);
        Systema systema = systemaMapper.toEntity(systemaDTO);
        systema = systemaRepository.save(systema);
        return systemaMapper.toDto(systema);
    }

    @Override
    public SystemaDTO update(SystemaDTO systemaDTO) {
        log.debug("Request to update System : {}", systemaDTO);
        Systema systema = systemaMapper.toEntity(systemaDTO);
        systema = systemaRepository.save(systema);
        return systemaMapper.toDto(systema);
    }

    @Override
    public Optional<SystemaDTO> partialUpdate(SystemaDTO systemaDTO) {
        log.debug("Request to partially update System : {}", systemaDTO);

        return systemaRepository
            .findById(systemaDTO.getId())
            .map(existingSystema -> {
                systemaMapper.partialUpdate(existingSystema, systemaDTO);

                return existingSystema;
            })
            .map(systemaRepository::save)
            .map(systemaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Systems");
        return systemaRepository.findAllByDeletedIsFalse(pageable).map(systemaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemaDTO> findAllBasic(Pageable pageable) {
        log.debug("Request to get all Systems with basic info");
        return systemaRepository.findAllByDeletedIsFalse(pageable).map(systemaMapper::toDtoBasic);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemaDTO> findOne(Long id) {
        log.debug("Request to get System : {}", id);
        return systemaRepository.findById(id).map(systemaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete System : {}", id);
        Optional<Systema> systemaOptional = systemaRepository.findById(id);
        if (systemaOptional.isPresent()) {
            Systema systema = systemaOptional.orElseThrow();
            systema.setDeleted(true);
            systemaRepository.save(systema);
        }
    }
}
