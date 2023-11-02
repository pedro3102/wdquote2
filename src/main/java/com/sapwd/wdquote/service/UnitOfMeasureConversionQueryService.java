package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.UnitOfMeasureConversionRepository;
import com.sapwd.wdquote.service.criteria.UnitOfMeasureConversionCriteria;
import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
import com.sapwd.wdquote.service.mapper.UnitOfMeasureConversionMapper;
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
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Service for executing complex queries for {@link UnitOfMeasureConversion} entities in the database.
 * The main input is a {@link UnitOfMeasureConversionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UnitOfMeasureConversionDTO} or a {@link Page} of {@link UnitOfMeasureConversionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UnitOfMeasureConversionQueryService extends QueryService<UnitOfMeasureConversion> {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureConversionQueryService.class);

    private final UnitOfMeasureConversionRepository unitOfMeasureConversionRepository;

    private final UnitOfMeasureConversionMapper unitOfMeasureConversionMapper;

    public UnitOfMeasureConversionQueryService(
        UnitOfMeasureConversionRepository unitOfMeasureConversionRepository,
        UnitOfMeasureConversionMapper unitOfMeasureConversionMapper
    ) {
        this.unitOfMeasureConversionRepository = unitOfMeasureConversionRepository;
        this.unitOfMeasureConversionMapper = unitOfMeasureConversionMapper;
    }

    /**
     * Return a {@link List} of {@link UnitOfMeasureConversionDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UnitOfMeasureConversionDTO> findByCriteria(UnitOfMeasureConversionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UnitOfMeasureConversion> specification = createSpecification(criteria);
        return unitOfMeasureConversionMapper.toDto(unitOfMeasureConversionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UnitOfMeasureConversionDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitOfMeasureConversionDTO> findByCriteria(UnitOfMeasureConversionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UnitOfMeasureConversion> specification = createSpecification(criteria);
        return unitOfMeasureConversionRepository.findAll(specification, page).map(unitOfMeasureConversionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UnitOfMeasureConversionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UnitOfMeasureConversion> specification = createSpecification(criteria);
        return unitOfMeasureConversionRepository.count(specification);
    }

    /**
     * Function to convert {@link UnitOfMeasureConversionCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UnitOfMeasureConversion> createSpecification(UnitOfMeasureConversionCriteria criteria) {
        Specification<UnitOfMeasureConversion> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), UnitOfMeasureConversion_.deleted)
        );
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UnitOfMeasureConversion_.id));
            }
            if (criteria.getUomId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUomId(),
                            root -> root.join(UnitOfMeasureConversion_.uom, JoinType.LEFT).get(UnitOfMeasure_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
