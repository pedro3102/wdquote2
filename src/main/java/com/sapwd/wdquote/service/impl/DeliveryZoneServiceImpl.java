package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.DeliveryZone;
import com.sapwd.wdquote.repository.DeliveryZoneRepository;
import com.sapwd.wdquote.service.DeliveryZoneService;
import com.sapwd.wdquote.service.dto.DeliveryZoneDTO;
import com.sapwd.wdquote.service.mapper.DeliveryZoneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryZone}.
 */
@Service
@Transactional
public class DeliveryZoneServiceImpl implements DeliveryZoneService {

    private final Logger log = LoggerFactory.getLogger(DeliveryZoneServiceImpl.class);

    private final DeliveryZoneRepository deliveryZoneRepository;

    private final DeliveryZoneMapper deliveryZoneMapper;

    public DeliveryZoneServiceImpl(DeliveryZoneRepository deliveryZoneRepository, DeliveryZoneMapper deliveryZoneMapper) {
        this.deliveryZoneRepository = deliveryZoneRepository;
        this.deliveryZoneMapper = deliveryZoneMapper;
    }

    @Override
    public DeliveryZoneDTO save(DeliveryZoneDTO deliveryZoneDTO) {
        log.debug("Request to save DeliveryZone : {}", deliveryZoneDTO);
        DeliveryZone deliveryZone = deliveryZoneMapper.toEntity(deliveryZoneDTO);
        deliveryZone = deliveryZoneRepository.save(deliveryZone);
        return deliveryZoneMapper.toDto(deliveryZone);
    }

    @Override
    public DeliveryZoneDTO update(DeliveryZoneDTO deliveryZoneDTO) {
        log.debug("Request to update DeliveryZone : {}", deliveryZoneDTO);
        DeliveryZone deliveryZone = deliveryZoneMapper.toEntity(deliveryZoneDTO);
        deliveryZone = deliveryZoneRepository.save(deliveryZone);
        return deliveryZoneMapper.toDto(deliveryZone);
    }

    @Override
    public Optional<DeliveryZoneDTO> partialUpdate(DeliveryZoneDTO deliveryZoneDTO) {
        log.debug("Request to partially update DeliveryZone : {}", deliveryZoneDTO);

        return deliveryZoneRepository
            .findById(deliveryZoneDTO.getId())
            .map(existingDeliveryZone -> {
                deliveryZoneMapper.partialUpdate(existingDeliveryZone, deliveryZoneDTO);

                return existingDeliveryZone;
            })
            .map(deliveryZoneRepository::save)
            .map(deliveryZoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryZoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryZones");
        return deliveryZoneRepository.findAllByDeletedIsFalse(pageable).map(deliveryZoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryZoneDTO> findOne(Long id) {
        log.debug("Request to get DeliveryZone : {}", id);
        return deliveryZoneRepository.findById(id).map(deliveryZoneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryZone : {}", id);
        Optional<DeliveryZone> deliveryZoneOptional = deliveryZoneRepository.findById(id);
        if (deliveryZoneOptional.isPresent()) {
            DeliveryZone deliveryZone = deliveryZoneOptional.orElseThrow();
            deliveryZone.setDeleted(true);
            deliveryZoneRepository.save(deliveryZone);
        }
    }
}
