package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.MovementDetailsRepository;
import com.sapwd.wdquote.service.criteria.MovementDetailsCriteria;
import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
import com.sapwd.wdquote.service.mapper.MovementDetailsMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MovementDetails} entities in the database.
 * The main input is a {@link MovementDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovementDetailsDTO} or a {@link Page} of {@link MovementDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovementDetailsQueryService extends QueryService<MovementDetails> {

    private final Logger log = LoggerFactory.getLogger(MovementDetailsQueryService.class);

    private final MovementDetailsRepository movementDetailsRepository;

    private final MovementDetailsMapper movementDetailsMapper;

    public MovementDetailsQueryService(MovementDetailsRepository movementDetailsRepository, MovementDetailsMapper movementDetailsMapper) {
        this.movementDetailsRepository = movementDetailsRepository;
        this.movementDetailsMapper = movementDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link MovementDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovementDetailsDTO> findByCriteria(MovementDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MovementDetails> specification = createSpecification(criteria);
        return movementDetailsMapper.toDto(movementDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovementDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovementDetailsDTO> findByCriteria(MovementDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MovementDetails> specification = createSpecification(criteria);
        return movementDetailsRepository.findAll(specification, page).map(movementDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovementDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MovementDetails> specification = createSpecification(criteria);
        return movementDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link MovementDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MovementDetails> createSpecification(MovementDetailsCriteria criteria) {
        Specification<MovementDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MovementDetails_.id));
            }
            if (criteria.getUnitCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitCost(), MovementDetails_.unitCost));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), MovementDetails_.qty));
            }
            if (criteria.getSalePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePrice(), MovementDetails_.salePrice));
            }
            if (criteria.getVendorCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVendorCode(), MovementDetails_.vendorCode));
            }
            if (criteria.getInventoryQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryQty(), MovementDetails_.inventoryQty));
            }
            if (criteria.getMovementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMovementId(),
                            root -> root.join(MovementDetails_.movement, JoinType.LEFT).get(Movement_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(MovementDetails_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventoryId(),
                            root -> root.join(MovementDetails_.inventory, JoinType.LEFT).get(Inventory_.id)
                        )
                    );
            }
            if (criteria.getStockPositionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockPositionId(),
                            root -> root.join(MovementDetails_.stockPosition, JoinType.LEFT).get(StockPosition_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
