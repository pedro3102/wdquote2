package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.MovementType;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.repository.MovementTypeRepository;
import com.sapwd.wdquote.service.MovementTypeService;
import com.sapwd.wdquote.service.dto.MovementTypeDTO;
import com.sapwd.wdquote.service.mapper.MovementTypeMapper;
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
 * Service Implementation for managing {@link MovementType}.
 */
@Service
@Transactional
public class MovementTypeServiceImpl implements MovementTypeService {

    private final Logger log = LoggerFactory.getLogger(MovementTypeServiceImpl.class);

    private final MovementTypeRepository movementTypeRepository;

    private final MovementTypeMapper movementTypeMapper;

    public MovementTypeServiceImpl(MovementTypeRepository movementTypeRepository, MovementTypeMapper movementTypeMapper) {
        this.movementTypeRepository = movementTypeRepository;
        this.movementTypeMapper = movementTypeMapper;
    }

    @Override
    public MovementTypeDTO save(MovementTypeDTO movementTypeDTO) {
        log.debug("Request to save MovementType : {}", movementTypeDTO);
        MovementType movementType = movementTypeMapper.toEntity(movementTypeDTO);
        movementType = movementTypeRepository.save(movementType);
        return movementTypeMapper.toDto(movementType);
    }

    @Override
    public MovementTypeDTO update(MovementTypeDTO movementTypeDTO) {
        log.debug("Request to update MovementType : {}", movementTypeDTO);
        MovementType movementType = movementTypeMapper.toEntity(movementTypeDTO);
        movementType = movementTypeRepository.save(movementType);
        return movementTypeMapper.toDto(movementType);
    }

    @Override
    public Optional<MovementTypeDTO> partialUpdate(MovementTypeDTO movementTypeDTO) {
        log.debug("Request to partially update MovementType : {}", movementTypeDTO);

        return movementTypeRepository
            .findById(movementTypeDTO.getId())
            .map(existingMovementType -> {
                movementTypeMapper.partialUpdate(existingMovementType, movementTypeDTO);

                return existingMovementType;
            })
            .map(movementTypeRepository::save)
            .map(movementTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovementTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MovementTypes");
        return movementTypeRepository.findAll(pageable).map(movementTypeMapper::toDto);
    }

    @Override
    public List<MovementTypeDTO> findAllBasic(OperationType type) {
        log.debug("Request to get all MovementTypes with basic info");
        if (type == null) {
            return movementTypeRepository
                .findAllByDeletedIsFalse()
                .stream()
                .map(movementTypeMapper::toDtoBasic)
                .collect(Collectors.toList());
        } else {
            return movementTypeRepository
                .findAllByTypeAndDeletedIsFalse(type)
                .stream()
                .map(movementTypeMapper::toDtoBasic)
                .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovementTypeDTO> findOne(Long id) {
        log.debug("Request to get MovementType : {}", id);
        return movementTypeRepository.findById(id).map(movementTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MovementType : {}", id);
        Optional<MovementType> movementTypeOptional = movementTypeRepository.findById(id);
        if (movementTypeOptional.isPresent()) {
            MovementType movementType = movementTypeOptional.orElseThrow();
            movementType.setDeleted(true);
            movementTypeRepository.save(movementType);
        }
    }
}
