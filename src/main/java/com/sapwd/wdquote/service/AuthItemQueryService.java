package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.domain.AuthItem_;
import com.sapwd.wdquote.repository.AuthItemRepository;
import com.sapwd.wdquote.service.criteria.AuthItemCriteria;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.service.mapper.AuthItemMapper;
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
 * Service for executing complex queries for {@link com.sapwd.wdquote.domain.AuthItem} entities in the database.
 * The main input is a {@link com.sapwd.wdquote.service.criteria.AuthItemCriteria}
 * which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link com.sapwd.wdquote.domain.AuthItem}
 * which fulfills the criteria.
 */
@Service
@Transactional
public class AuthItemQueryService extends QueryService<AuthItem> {

    private final Logger log = LoggerFactory.getLogger(AuthItemQueryService.class);
    private final AuthItemRepository authItemRepository;
    private final AuthItemMapper authItemMapper;

    public AuthItemQueryService(AuthItemRepository authItemRepository, AuthItemMapper authItemMapper) {
        this.authItemRepository = authItemRepository;
        this.authItemMapper = authItemMapper;
    }

    /**
     * Return a {@link List} of {@link AuthItem} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthItemDTO> findByCriteria(AuthItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuthItem> specification = createSpecification(criteria);
        return authItemMapper.toDto(authItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuthItem} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthItemDTO> findByCriteria(AuthItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuthItem> specification = createSpecification(criteria);
        return authItemRepository.findAll(specification, page).map(authItemMapper::toDto);
    }

    /**
     * Function to convert {@link AuthItemCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuthItem> createSpecification(AuthItemCriteria criteria) {
        Specification<AuthItem> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), AuthItem_.deleted)
        );
        //    specification = specification.and(buildMultiTenantSpecification(criteria, AuthItem_.organizationId));
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuthItem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AuthItem_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AuthItem_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AuthItem_.type));
            }
            // once all AND are done we add a last AND with all ORs
            if (criteria.getGlobalFilter() != null) {
                Specification<AuthItem> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobalFilter(), AuthItem_.name));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobalFilter(), AuthItem_.description));
                specification = specification.and(orSpecification);
            }
            // once all AND are done we add a last AND with all ORs
            if (criteria.getGlobalId() != null) {
                Specification<AuthItem> orSpecification = Specification.where(null);
                orSpecification = orSpecification.and(buildSpecification(criteria.getGlobalId(), AuthItem_.id));
                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }
}
