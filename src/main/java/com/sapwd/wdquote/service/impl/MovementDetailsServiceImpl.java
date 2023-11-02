package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.MovementDetails;
import com.sapwd.wdquote.repository.MovementDetailsRepository;
import com.sapwd.wdquote.service.MovementDetailsService;
import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
import com.sapwd.wdquote.service.mapper.MovementDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MovementDetails}.
 */
@Service
@Transactional
public class MovementDetailsServiceImpl implements MovementDetailsService {

    private final Logger log = LoggerFactory.getLogger(MovementDetailsServiceImpl.class);

    private final MovementDetailsRepository movementDetailsRepository;

    private final MovementDetailsMapper movementDetailsMapper;

    public MovementDetailsServiceImpl(MovementDetailsRepository movementDetailsRepository, MovementDetailsMapper movementDetailsMapper) {
        this.movementDetailsRepository = movementDetailsRepository;
        this.movementDetailsMapper = movementDetailsMapper;
    }

    @Override
    public MovementDetailsDTO save(MovementDetailsDTO movementDetailsDTO) {
        log.debug("Request to save MovementDetails : {}", movementDetailsDTO);
        MovementDetails movementDetails = movementDetailsMapper.toEntity(movementDetailsDTO);
        movementDetails = movementDetailsRepository.save(movementDetails);
        return movementDetailsMapper.toDto(movementDetails);
    }

    @Override
    public MovementDetailsDTO update(MovementDetailsDTO movementDetailsDTO) {
        log.debug("Request to update MovementDetails : {}", movementDetailsDTO);
        MovementDetails movementDetails = movementDetailsMapper.toEntity(movementDetailsDTO);
        movementDetails = movementDetailsRepository.save(movementDetails);
        return movementDetailsMapper.toDto(movementDetails);
    }

    @Override
    public Optional<MovementDetailsDTO> partialUpdate(MovementDetailsDTO movementDetailsDTO) {
        log.debug("Request to partially update MovementDetails : {}", movementDetailsDTO);

        return movementDetailsRepository
            .findById(movementDetailsDTO.getId())
            .map(existingMovementDetails -> {
                movementDetailsMapper.partialUpdate(existingMovementDetails, movementDetailsDTO);

                return existingMovementDetails;
            })
            .map(movementDetailsRepository::save)
            .map(movementDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovementDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MovementDetails");
        return movementDetailsRepository.findAll(pageable).map(movementDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovementDetailsDTO> findOne(Long id) {
        log.debug("Request to get MovementDetails : {}", id);
        return movementDetailsRepository.findById(id).map(movementDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MovementDetails : {}", id);
        Optional<MovementDetails> movementDetailsOptional = movementDetailsRepository.findById(id);
        if (movementDetailsOptional.isPresent()) {
            MovementDetails movementDetails = movementDetailsOptional.orElseThrow();
            movementDetails.setDeleted(true);
            movementDetailsRepository.save(movementDetails);
        }
    }
}
