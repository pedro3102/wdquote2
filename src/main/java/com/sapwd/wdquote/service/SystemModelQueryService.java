package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.SystemModelRepository;
import com.sapwd.wdquote.service.criteria.SystemModelCriteria;
import com.sapwd.wdquote.service.dto.SystemModelDTO;
import com.sapwd.wdquote.service.mapper.SystemModelMapper;
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
 * Service for executing complex queries for {@link SystemModel} entities in the database.
 * The main input is a {@link SystemModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemModelDTO} or a {@link Page} of {@link SystemModelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemModelQueryService extends QueryService<SystemModel> {

    private final Logger log = LoggerFactory.getLogger(SystemModelQueryService.class);

    private final SystemModelRepository systemModelRepository;

    private final SystemModelMapper systemModelMapper;

    public SystemModelQueryService(SystemModelRepository systemModelRepository, SystemModelMapper systemModelMapper) {
        this.systemModelRepository = systemModelRepository;
        this.systemModelMapper = systemModelMapper;
    }

    /**
     * Return a {@link List} of {@link SystemModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemModelDTO> findByCriteria(SystemModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemModel> specification = createSpecification(criteria);
        return systemModelMapper.toDto(systemModelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemModelDTO> findByCriteria(SystemModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemModel> specification = createSpecification(criteria);
        return systemModelRepository.findAll(specification, page).map(systemModelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemModel> specification = createSpecification(criteria);
        return systemModelRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemModel> createSpecification(SystemModelCriteria criteria) {
        Specification<SystemModel> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), SystemModel_.deleted)
        );
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SystemModel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SystemModel_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SystemModel_.description));
            }
            if (criteria.getPicture() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicture(), SystemModel_.picture));
            }
            if (criteria.getSystemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSystemId(), root -> root.join(SystemModel_.system, JoinType.LEFT).get(Systema_.id))
                    );
            }
            if (criteria.getSystem() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSystem(), root -> root.join(SystemModel_.system, JoinType.LEFT).get(Systema_.name))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(SystemModel_.products, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getGlobal() != null) {
                Specification<SystemModel> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), SystemModel_.name));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), SystemModel_.description));
                orSpecification =
                    orSpecification.or(
                        buildSpecification(criteria.getGlobal(), root -> root.join(SystemModel_.system, JoinType.LEFT).get(Systema_.name))
                    );

                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }
}
