package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.VendorRepository;
import com.sapwd.wdquote.service.criteria.VendorCriteria;
import com.sapwd.wdquote.service.dto.VendorDTO;
import com.sapwd.wdquote.service.mapper.VendorMapper;
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
 * Service for executing complex queries for {@link Vendor} entities in the database.
 * The main input is a {@link VendorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VendorDTO} or a {@link Page} of {@link VendorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VendorQueryService extends QueryService<Vendor> {

    private final Logger log = LoggerFactory.getLogger(VendorQueryService.class);

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorQueryService(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    /**
     * Return a {@link List} of {@link VendorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VendorDTO> findByCriteria(VendorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vendor> specification = createSpecification(criteria);
        return vendorMapper.toDto(vendorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VendorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VendorDTO> findByCriteria(VendorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vendor> specification = createSpecification(criteria);
        return vendorRepository.findAll(specification, page).map(vendorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VendorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vendor> specification = createSpecification(criteria);
        return vendorRepository.count(specification);
    }

    /**
     * Function to convert {@link VendorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vendor> createSpecification(VendorCriteria criteria) {
        Specification<Vendor> specification = Specification.where(buildSpecification(new BooleanFilter().setEquals(false), User_.deleted));
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vendor_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Vendor_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Vendor_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Vendor_.address));
            }
            if (criteria.getGlobal() != null) {
                Specification<Vendor> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Vendor_.code));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Vendor_.name));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Vendor_.address));
                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }
}
