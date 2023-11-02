package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.StockPosition;
import com.sapwd.wdquote.repository.StockPositionRepository;
import com.sapwd.wdquote.service.StockPositionService;
import com.sapwd.wdquote.service.dto.StockPositionDTO;
import com.sapwd.wdquote.service.mapper.StockPositionMapper;
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
 * Service Implementation for managing {@link StockPosition}.
 */
@Service
@Transactional
public class StockPositionServiceImpl implements StockPositionService {

    private final Logger log = LoggerFactory.getLogger(StockPositionServiceImpl.class);

    private final StockPositionRepository stockPositionRepository;

    private final StockPositionMapper stockPositionMapper;

    public StockPositionServiceImpl(StockPositionRepository stockPositionRepository, StockPositionMapper stockPositionMapper) {
        this.stockPositionRepository = stockPositionRepository;
        this.stockPositionMapper = stockPositionMapper;
    }

    @Override
    public StockPositionDTO save(StockPositionDTO stockPositionDTO) {
        log.debug("Request to save StockPosition : {}", stockPositionDTO);
        StockPosition stockPosition = stockPositionMapper.toEntity(stockPositionDTO);
        stockPosition = stockPositionRepository.save(stockPosition);
        return stockPositionMapper.toDto(stockPosition);
    }

    @Override
    public StockPositionDTO update(StockPositionDTO stockPositionDTO) {
        log.debug("Request to update StockPosition : {}", stockPositionDTO);
        StockPosition stockPosition = stockPositionMapper.toEntity(stockPositionDTO);
        stockPosition = stockPositionRepository.save(stockPosition);
        return stockPositionMapper.toDto(stockPosition);
    }

    @Override
    public Optional<StockPositionDTO> partialUpdate(StockPositionDTO stockPositionDTO) {
        log.debug("Request to partially update StockPosition : {}", stockPositionDTO);

        return stockPositionRepository
            .findById(stockPositionDTO.getId())
            .map(existingStockPosition -> {
                stockPositionMapper.partialUpdate(existingStockPosition, stockPositionDTO);

                return existingStockPosition;
            })
            .map(stockPositionRepository::save)
            .map(stockPositionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockPositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockPositions");
        return stockPositionRepository.findAll(pageable).map(stockPositionMapper::toDto);
    }

    @Override
    public List<StockPositionDTO> findAllBasic() {
        log.debug("Request to get all Locations basic");
        return stockPositionRepository.findAllByDeletedIsFalse().stream().map(stockPositionMapper::toDtoBasic).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockPositionDTO> findOne(Long id) {
        log.debug("Request to get StockPosition : {}", id);
        return stockPositionRepository.findById(id).map(stockPositionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockPosition : {}", id);
        Optional<StockPosition> stockPositionOptional = stockPositionRepository.findById(id);
        if (stockPositionOptional.isPresent()) {
            StockPosition stockPosition = stockPositionOptional.orElseThrow();
            stockPosition.setDeleted(true);
            stockPositionRepository.save(stockPosition);
        }
    }
}
