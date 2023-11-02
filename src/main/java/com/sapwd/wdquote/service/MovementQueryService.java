package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.MovementRepository;
import com.sapwd.wdquote.service.criteria.MovementCriteria;
import com.sapwd.wdquote.service.dto.MovementDTO;
import com.sapwd.wdquote.service.mapper.MovementMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;

/**
 * Service for executing complex queries for {@link Movement} entities in the database.
 * The main input is a {@link MovementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovementDTO} or a {@link Page} of {@link MovementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovementQueryService extends QueryService<Movement> {

    private final Logger log = LoggerFactory.getLogger(MovementQueryService.class);

    private final MovementRepository movementRepository;

    private final MovementMapper movementMapper;

    public MovementQueryService(MovementRepository movementRepository, MovementMapper movementMapper) {
        this.movementRepository = movementRepository;
        this.movementMapper = movementMapper;
    }

    /**
     * Return a {@link List} of {@link MovementDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovementDTO> findByCriteria(MovementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Movement> specification = createSpecification(criteria);
        return movementMapper.toDto(movementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovementDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovementDTO> findByCriteria(MovementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Movement> specification = createSpecification(criteria);
        return movementRepository.findAll(specification, page).map(movementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Movement> specification = createSpecification(criteria);
        return movementRepository.count(specification);
    }

    /**
     * Function to convert {@link MovementCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Movement> createSpecification(MovementCriteria criteria) {
        Specification<Movement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Movement_.id));
            }
            if (criteria.getNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNo(), Movement_.no));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Movement_.reference));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Movement_.date));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Movement_.note));
            }
            if (criteria.getCanceledDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCanceledDate(), Movement_.canceledDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Movement_.status));
            }
            if (criteria.getMovementTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMovementTypeId(),
                            root -> root.join(Movement_.movementType, JoinType.LEFT).get(MovementType_.id)
                        )
                    );
            }
            if (criteria.getMovementType() != null) {
                Specification<Movement> orSpecification = Specification.where(null);
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getMovementType(), root -> root.join(Movement_.movementType, JoinType.LEFT).get(MovementType_.code))
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getMovementType(), root -> root.join(Movement_.movementType, JoinType.LEFT).get(MovementType_.name))
                    );

                specification = specification.and(orSpecification);
            }
            if (criteria.getLocation() != null) {
                Specification<Movement> orSpecification = Specification.where(null);
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getLocation(), root -> root.join(Movement_.location, JoinType.LEFT).get(Location_.code))
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getLocation(), root -> root.join(Movement_.location, JoinType.LEFT).get(Location_.name))
                    );

                specification = specification.and(orSpecification);
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLocationId(), root -> root.join(Movement_.location, JoinType.LEFT).get(Location_.id))
                    );
            }
            if (criteria.getCounterpartLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCounterpartLocationId(),
                            root -> root.join(Movement_.counterpartLocation, JoinType.LEFT).get(Location_.id)
                        )
                    );
            }
            if (criteria.getCounterpartVendorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCounterpartVendorId(),
                            root -> root.join(Movement_.counterpartVendor, JoinType.LEFT).get(Vendor_.id)
                        )
                    );
            }
            if (criteria.getCounterpartCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCounterpartCustomerId(),
                            root -> root.join(Movement_.counterpartCustomer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }

            if (criteria.getCounterpart() != null) {
                Specification<Movement> orSpecification = Specification.where(null);
                orSpecification = getCounterpartSpecification(criteria.getCounterpart(), orSpecification);

                specification = specification.and(orSpecification);
            }

            if (criteria.getGlobal() != null) {
                Specification<Movement> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Movement_.no));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Movement_.reference));
                //orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Movement_.date));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Movement_.note));
                //orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Movement_.canceledDate));
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Movement_.movementType, JoinType.LEFT).get(MovementType_.code)
                        )
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Movement_.movementType, JoinType.LEFT).get(MovementType_.name)
                        )
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Movement_.location, JoinType.LEFT).get(Location_.code)
                        )
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Movement_.location, JoinType.LEFT).get(Location_.name)
                        )
                    );

                orSpecification = getCounterpartSpecification(criteria.getGlobal(), orSpecification);

                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }

    private Specification<Movement> getCounterpartSpecification(StringFilter criteria, Specification<Movement> orSpecification) {
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartVendor, JoinType.LEFT).get(Vendor_.code))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartVendor, JoinType.LEFT).get(Vendor_.name))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartLocation, JoinType.LEFT).get(Location_.code))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartLocation, JoinType.LEFT).get(Location_.name))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartCustomer, JoinType.LEFT).get(Customer_.code))
            );
        orSpecification =
            orSpecification.or(
                buildSpecification(
                    criteria, root -> root.join(Movement_.counterpartCustomer, JoinType.LEFT).get(Customer_.name))
            );
        return orSpecification;
    }
}
