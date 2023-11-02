package com.sapwd.wdquote.service.impl;

import static com.sapwd.wdquote.service.util.SystemUtils.DEFAULT_ROUND_MODE;

import com.sapwd.wdquote.domain.Inventory;
import com.sapwd.wdquote.domain.Movement;
import com.sapwd.wdquote.domain.MovementDetails;
import com.sapwd.wdquote.domain.MovementType;
import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.repository.MovementDetailsRepository;
import com.sapwd.wdquote.repository.MovementRepository;
import com.sapwd.wdquote.service.InventoryService;
import com.sapwd.wdquote.service.MovementService;
import com.sapwd.wdquote.service.dto.MovementDTO;
import com.sapwd.wdquote.service.mapper.MovementMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Movement}.
 */
@Service
@Transactional
public class MovementServiceImpl implements MovementService {

    private final Logger log = LoggerFactory.getLogger(MovementServiceImpl.class);

    private final String formatPre = "%s%ty";
    private final String format = formatPre + "%05d";

    private final MovementRepository movementRepository;

    private final MovementDetailsRepository movementDetailsRepository;

    private final MovementMapper movementMapper;

    private final InventoryService inventoryService;

    public MovementServiceImpl(
        MovementRepository movementRepository,
        MovementDetailsRepository movementDetailsRepository,
        MovementMapper movementMapper,
        InventoryService inventoryService
    ) {
        this.movementRepository = movementRepository;
        this.movementDetailsRepository = movementDetailsRepository;
        this.movementMapper = movementMapper;
        this.inventoryService = inventoryService;
    }

    @Override
    public MovementDTO save(MovementDTO movementDTO) {
        log.debug("Request to save Movement : {}", movementDTO);
        Movement movement = movementMapper.toEntity(movementDTO);

        movement.setNo(generateMovNoPending(movement.getMovementType()));
        movement.setConsecutive(0L);

        movement = movementRepository.save(movement);
        return movementMapper.toDto(movement);
    }

    @Override
    public MovementDTO update(MovementDTO movementDTO) {
        log.debug("Request to update Movement : {}", movementDTO);
        Movement movement = movementMapper.toEntity(movementDTO);
        movement = movementRepository.save(movement);
        return movementMapper.toDto(movement);
    }

    @Override
    public Optional<MovementDTO> partialUpdate(MovementDTO movementDTO) {
        log.debug("Request to partially update Movement : {}", movementDTO);

        return movementRepository
            .findById(movementDTO.getId())
            .map(existingMovement -> {
                movementMapper.partialUpdate(existingMovement, movementDTO);

                return existingMovement;
            })
            .map(movementRepository::save)
            .map(movementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Movements");
        return movementRepository.findAll(pageable).map(movementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovementDTO> findOne(Long id) {
        log.debug("Request to get Movement : {}", id);
        return movementRepository.findById(id).map(movementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movement : {}", id);
        Optional<Movement> movementOptional = movementRepository.findById(id);
        if (movementOptional.isPresent() && movementOptional.get().getStatus().equals(MovementStatus.PENDING)) {
            movementRepository.deleteById(id);
        }
    }

    @Override
    public void deleteDetails(Long id) {
        log.debug("Request to delete all details of a Movement by id : {}", id);
        Optional<Movement> movementOptional = movementRepository.findById(id);
        if (movementOptional.isPresent() && movementOptional.get().getStatus().equals(MovementStatus.PENDING)) {
            List<MovementDetails> movementDetails = movementDetailsRepository.findAllByMovement(movementOptional.get());
            movementDetailsRepository.deleteAllById(movementDetails.stream().map(MovementDetails::getId).collect(Collectors.toList()));
        }
    }

    @Transactional
    public Movement confirmMovement(Movement movement) throws Exception {
        if (!movement.getStatus().equals(MovementStatus.PENDING)) {
            log.error("Only PENDING movements can be completed. Movement: {}", movement.getNo());
            throw new Exception("Only PENDING movement can be completed");
        }

        if (movement.getMovementDetailsCollection().isEmpty()) {
            log.error("Movement {} hasn't details", movement.getNo());
            throw new Exception("Movement without details");
        }

        movement.setNo(generateMovNo(movement.getMovementType()));
        movement.setStatus(MovementStatus.COMPLETED);
        movement.setCompleted(movement.getStatus().equals(MovementStatus.COMPLETED));
        movement.setConsecutive(nextGeneralConsecutive());

        //actualizar inventario
        Map<Inventory, BigDecimal> inventoriesMap = new HashMap<>();
        if (movement.getMovementType().getType().equals(OperationType.OUT)) {
            processOutput(movement, inventoriesMap);
        } else {
            processInput(movement, inventoriesMap);
        }

        for (Inventory inventory : inventoriesMap.keySet()) {
            if (
                movement.getMovementType().getType().equals(OperationType.OUT) &&
                inventory.getQty().compareTo(inventoriesMap.get(inventory)) < 0
            ) {
                log.error("There are not enough units of product {} in inventory", movement.getNo());
                throw new Exception("There are not enough units of product in inventory");
            }
            inventory.setQty(inventory.getQty().add(inventoriesMap.get(inventory)));
            inventory.setLastActivityDate(LocalDate.now());
        }

        return movement;
    }

    private Long nextGeneralConsecutive() {
        Long maxConsecutive = movementRepository.findMaxConsecutive();
        return ++maxConsecutive;
    }

    private void processInput(Movement movement, Map<Inventory, BigDecimal> inventoriesMap) {
        for (MovementDetails movementDetails : movement.getMovementDetailsCollection()) {
            Inventory inventory = movementDetails.getInventory();
            if (inventory == null) { //find inventory
                List<Inventory> inventoryList = inventoryService.findByProductLocation(
                    movementDetails.getProduct(),
                    movement.getLocation()
                );
                if (inventoryList.isEmpty()) {
                    //                    log.error("Error in the movement, there are no inventories for product {} and location {}",
                    //                        movementDetails.getProduct().getCode(), movement.getLocation().getCode());
                    //                    throw new Exception("Error in the movement, there are no inventories for product and area");
                    //create inventory
                    inventory = new Inventory();
                    inventory.setProduct(movementDetails.getProduct());
                    inventory.setLocation(movement.getLocation());
                    inventory.setUnitCost(movementDetails.getUnitCost());
                    inventory.setReorderPoint(BigDecimal.ZERO);
                    inventory.setQty(BigDecimal.ZERO);
                    inventory.setLastActivityDate(LocalDate.now());
                    inventory = inventoryService.save(inventory);
                } else {
                    inventory = inventoryList.get(0);
                }
                movementDetails.setInventory(inventory);
            }
            if (inventoriesMap.containsKey(inventory)) {
                inventoriesMap.put(inventory, inventoriesMap.get(inventory).add(movementDetails.getQty()));
            } else {
                inventoriesMap.put(inventory, movementDetails.getQty());
            }
            movementDetails.setInventoryQty(inventory.getQty().add(inventoriesMap.get(inventory)));
            averageCosts(movementDetails, inventory);
        }
    }

    private void processOutput(Movement movement, Map<Inventory, BigDecimal> inventoriesMap) throws Exception {
        if (movement.getMovementType().getId().equals(4L)) { //Internal Output Transfer
            //create Internal Input Transfer
            Movement oppositeMovement = createOppositeMovement(movement);
            confirmMovement(oppositeMovement);
        }
        for (MovementDetails movementDetails : movement.getMovementDetailsCollection()) {
            Inventory inventory = movementDetails.getInventory();
            if (inventoriesMap.containsKey(inventory)) {
                inventoriesMap.put(inventory, inventoriesMap.get(inventory).subtract(movementDetails.getQty()));
            } else {
                inventoriesMap.put(inventory, movementDetails.getQty().negate());
            }
            movementDetails.setInventoryQty(inventory.getQty().add(inventoriesMap.get(inventory)));
        }
    }

    private Movement createOppositeMovement(Movement movement) {
        Movement opposite = new Movement();
        opposite.setStatus(MovementStatus.PENDING);
        opposite.setMovementType(movement.getMovementType().getOppositeMovementType());
        opposite.setType(opposite.getMovementType().getType());
        opposite.setNo(generateMovNoPending(opposite.getMovementType()));
        opposite.setReference(movement.getNo());
        opposite.setDate(movement.getDate());
        opposite.setCounterpart(movement.getCounterpart());
        opposite.setLocation(movement.getCounterpartLocation());
        opposite.setCounterpartLocation(movement.getLocation());
        for (MovementDetails movementDetails : movement.getMovementDetailsCollection()) {
            MovementDetails oppositeDetail = createOppositeDetail(movementDetails);
            oppositeDetail.setMovement(opposite);
            opposite.getMovementDetailsCollection().add(oppositeDetail);
        }
        return movementRepository.save(opposite);
    }

    private MovementDetails createOppositeDetail(MovementDetails movementDetails) {
        MovementDetails oppositeDetail = new MovementDetails();
        oppositeDetail.setUnitCost(movementDetails.getUnitCost());
        oppositeDetail.setQty(movementDetails.getQty());
        oppositeDetail.setInventoryQty(BigDecimal.ZERO);
        oppositeDetail.setSalePrice(movementDetails.getSalePrice());
        oppositeDetail.setVendorCode(movementDetails.getVendorCode());
        oppositeDetail.setProduct(movementDetails.getProduct());

        return oppositeDetail;
    }

    private void averageCosts(MovementDetails movementDetails, Inventory inventory) {
        BigDecimal qty = movementDetails.getQty();
        BigDecimal unitCost = movementDetails.getUnitCost();
        BigDecimal importCost = qty.multiply(unitCost).setScale(5, DEFAULT_ROUND_MODE);

        BigDecimal qtyTotal = qty.add(inventory.getQty()).setScale(5, DEFAULT_ROUND_MODE);
        importCost = importCost.add(inventory.getQty().multiply(inventory.getUnitCost())).setScale(5, DEFAULT_ROUND_MODE);

        if (qty.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal avgCost = importCost.divide(qtyTotal, 5, DEFAULT_ROUND_MODE);
            inventory.setUnitCost(avgCost);
        }
    }

    @Override
    public MovementDTO confirmMovement(Long id) throws Exception {
        Optional<Movement> optionalMovement = movementRepository.findById(id);
        if (optionalMovement.isEmpty()) {
            return null;
        }
        return movementMapper.toDto(confirmMovement(optionalMovement.get()));
    }

    private String generateMovNo(MovementType movementType) {
        return String.format(format, movementType.getCode(), LocalDate.now(), getConsecutive(true, movementType));
    }

    private String generateMovNoPending(MovementType movementType) {
        return String.format("P" + format, movementType.getCode(), LocalDate.now(), getConsecutive(false, movementType));
    }

    private int getConsecutive(boolean completed, MovementType movementType) {
        String prefix = completed ? formatPre : "P" + formatPre;
        Movement movement = movementRepository.findTop1ByCompletedAndNoStartsWithOrderByCreatedDateDesc(
            completed,
            String.format(prefix, movementType.getCode(), LocalDate.now())
        );

        int consecutive = 0;
        if (movement != null) {
            String noMovement = movement.getNo();
            consecutive = Integer.parseInt(noMovement.substring(noMovement.length() - 5));
        }

        return ++consecutive;
    }
}
