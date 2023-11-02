package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.SystemModel;
import com.sapwd.wdquote.repository.SystemModelRepository;
import com.sapwd.wdquote.service.SystemModelService;
import com.sapwd.wdquote.service.dto.SystemModelDTO;
import com.sapwd.wdquote.service.mapper.SystemModelMapper;
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
 * Service Implementation for managing {@link SystemModel}.
 */
@Service
@Transactional
public class SystemModelServiceImpl implements SystemModelService {

    private final Logger log = LoggerFactory.getLogger(SystemModelServiceImpl.class);

    private final SystemModelRepository systemModelRepository;

    private final SystemModelMapper systemModelMapper;

    public SystemModelServiceImpl(SystemModelRepository systemModelRepository, SystemModelMapper systemModelMapper) {
        this.systemModelRepository = systemModelRepository;
        this.systemModelMapper = systemModelMapper;
    }

    @Override
    public SystemModelDTO save(SystemModelDTO systemModelDTO) {
        log.debug("Request to save SystemModel : {}", systemModelDTO);
        SystemModel systemModel = systemModelMapper.toEntity(systemModelDTO);
        systemModel = systemModelRepository.save(systemModel);
        return systemModelMapper.toDto(systemModel);
    }

    @Override
    public SystemModelDTO update(SystemModelDTO systemModelDTO) {
        log.debug("Request to update SystemModel : {}", systemModelDTO);
        SystemModel systemModel = systemModelMapper.toEntity(systemModelDTO);
        systemModel = systemModelRepository.save(systemModel);
        return systemModelMapper.toDto(systemModel);
    }

    @Override
    public Optional<SystemModelDTO> partialUpdate(SystemModelDTO systemModelDTO) {
        log.debug("Request to partially update SystemModel : {}", systemModelDTO);

        return systemModelRepository
            .findById(systemModelDTO.getId())
            .map(existingSystemModel -> {
                systemModelMapper.partialUpdate(existingSystemModel, systemModelDTO);

                return existingSystemModel;
            })
            .map(systemModelRepository::save)
            .map(systemModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemModels");
        return systemModelRepository.findAllByDeletedIsFalse(pageable).map(systemModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemModelDTO> findOne(Long id) {
        log.debug("Request to get SystemModel : {}", id);
        return systemModelRepository.findById(id).map(systemModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemModel : {}", id);
        Optional<SystemModel> systemModelOptional = systemModelRepository.findById(id);
        if (systemModelOptional.isPresent()) {
            SystemModel systemModel = systemModelOptional.orElseThrow();
            systemModel.setDeleted(true);
            systemModelRepository.save(systemModel);
        }
    }

    @Override
    public List<SystemModelDTO> findAllBasic() {
        log.debug("Request to get all SystemModel with basic info");
        return systemModelRepository
            .findAllByDeletedIsFalse(null)
            .stream()
            .map(systemModelMapper::toDtoSystemModelBasic)
            .collect(Collectors.toList());
    }
}
