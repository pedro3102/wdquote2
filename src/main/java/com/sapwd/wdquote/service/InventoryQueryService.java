package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.InventoryRepository;
import com.sapwd.wdquote.service.criteria.InventoryCriteria;
import com.sapwd.wdquote.service.dto.InventoryDTO;
import com.sapwd.wdquote.service.mapper.InventoryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;

/**
 * Service for executing complex queries for {@link Inventory} entities in the database.
 * The main input is a {@link InventoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InventoryDTO} or a {@link Page} of {@link InventoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryQueryService extends QueryService<Inventory> {

    private final Logger log = LoggerFactory.getLogger(InventoryQueryService.class);

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryQueryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    /**
     * Return a {@link List} of {@link InventoryDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> findByCriteria(InventoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inventory> specification = createSpecification(criteria);
        return inventoryMapper.toDto(inventoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InventoryDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryDTO> findByCriteria(InventoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inventory> specification = createSpecification(criteria);
        return inventoryRepository.findAll(specification, page).map(inventoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inventory> specification = createSpecification(criteria);
        return inventoryRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inventory> createSpecification(InventoryCriteria criteria) {
        Specification<Inventory> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), Inventory_.deleted)
        );
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inventory_.id));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), Inventory_.qty));
            }
            if (criteria.getUnitCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitCost(), Inventory_.unitCost));
            }
            if (criteria.getLastActivityDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastActivityDate(), Inventory_.lastActivityDate));
            }
            if (criteria.getShelf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShelf(), Inventory_.shelf));
            }
            if (criteria.getReorderPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReorderPoint(), Inventory_.reorderPoint));
            }

            if (criteria.getStockStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStockStatus(), Inventory_.stockStatus));
            }
            //            if (criteria.getStockStatus() != null) {
            //                if (criteria.getStockStatus().getEquals() != null) {
            //                    if (criteria.getStockStatus().getEquals().equals(StockStatus.IN_STOCK)) {
            //                        specification = specification.and((root, query, builder) -> builder.ge(root.get(Inventory_.qty), root.get(Inventory_.reorderPoint)));
            //                    } else if (criteria.getStockStatus().getEquals().equals(StockStatus.LOW_STOCK)) {
            //                        specification = specification
            //                            .and((root, query, builder) -> builder.greaterThan(root.get(Inventory_.qty), BigDecimal.ZERO))
            //                            .and((root, query, builder) -> builder.le(root.get(Inventory_.qty), root.get(Inventory_.reorderPoint))
            //                            );
            //                    } else if (criteria.getStockStatus().getEquals().equals(StockStatus.OUT_STOCK)) {
            //                        specification = specification.and((root, query, builder) -> builder.le(root.get(Inventory_.qty), 0));
            //                    }
            //                }
            //            }
            if (criteria.getVendorLeadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVendorLeadTime(), Inventory_.vendorLeadTime));
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationId(),
                            root -> root.join(Inventory_.location, JoinType.LEFT).get(Location_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(Inventory_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getCode() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCode(), root -> root.join(Inventory_.product, JoinType.LEFT).get(Product_.code))
                    );
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDescription(),
                            root -> root.join(Inventory_.product, JoinType.LEFT).get(Product_.description)
                        )
                    );
            }
            if (criteria.getUom() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUom(),
                            root ->
                                root
                                    .join(Inventory_.product, JoinType.LEFT)
                                    .join(Product_.uom, JoinType.LEFT)
                                    .get(UnitOfMeasure_.abbreviation)
                        )
                    );
            }

            if (criteria.getProduct() != null) {
                Specification<Inventory> orSpecification = Specification.where(null);

                orSpecification = getProductSpecification(criteria.getProduct(), orSpecification);

                specification = specification.and(orSpecification);
            }

            if (criteria.getGlobal() != null) {
                Specification<Inventory> orSpecification = Specification.where(null);

                orSpecification = getProductSpecification(criteria.getGlobal(), orSpecification);

                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getGlobal(), root -> root.join(Inventory_.location, JoinType.LEFT).get(Location_.code))
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getGlobal(), root -> root.join(Inventory_.location, JoinType.LEFT).get(Location_.name))
                    );

                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }

    private Specification<Inventory> getProductSpecification(StringFilter criteria, Specification<Inventory> orSpecification) {
        orSpecification =
            orSpecification.or(
                buildSpecification(criteria, root -> root.join(Inventory_.product, JoinType.LEFT).get(Product_.code))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria,
                    root -> root.join(Inventory_.product, JoinType.LEFT).get(Product_.description)
                )
            );
        return orSpecification;
    }
}
