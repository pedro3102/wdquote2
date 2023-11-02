package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.Inventory;
import com.sapwd.wdquote.domain.Location;
import com.sapwd.wdquote.domain.Product;
import com.sapwd.wdquote.repository.InventoryRepository;
import com.sapwd.wdquote.service.InventoryService;
import com.sapwd.wdquote.service.dto.InventoryDTO;
import com.sapwd.wdquote.service.mapper.InventoryMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inventory}.
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryDTO save(InventoryDTO inventoryDTO) {
        log.debug("Request to save Inventory by DTO : {}", inventoryDTO);
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory = save(inventory);
        return inventoryMapper.toDto(inventory);
    }

    @Override
    public Inventory save(Inventory inventory) {
        log.debug("Request to save Inventory : {}", inventory);
        if (inventory.getReorderPoint() == null) {
            inventory.setReorderPoint(BigDecimal.ZERO);
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    public InventoryDTO update(InventoryDTO inventoryDTO) {
        log.debug("Request to update Inventory : {}", inventoryDTO);
        return save(inventoryDTO);
    }

    @Override
    public Optional<InventoryDTO> partialUpdate(InventoryDTO inventoryDTO) {
        log.debug("Request to partially update Inventory : {}", inventoryDTO);

        return inventoryRepository
            .findById(inventoryDTO.getId())
            .map(existingInventory -> {
                inventoryMapper.partialUpdate(existingInventory, inventoryDTO);

                return existingInventory;
            })
            .map(this::save)
            .map(inventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inventories");
        return inventoryRepository.findAll(pageable).map(inventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryDTO> findOne(Long id) {
        log.debug("Request to get Inventory : {}", id);
        return inventoryRepository.findById(id).map(inventoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inventory : {}", id);
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.orElseThrow();
            inventory.setDeleted(true);
            save(inventory);
        }
    }

    @Override
    public List<Inventory> findByProductLocation(Product product, Location location) {
        log.debug("Request to get Inventory by Product {} and Location: {}", product.getCode(), location.getCode());
        return inventoryRepository.findAllByProductAndLocationAndDeletedIsFalseOrderByLastModifiedDateDesc(product, location);
    }
}
