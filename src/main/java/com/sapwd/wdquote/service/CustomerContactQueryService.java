package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.CustomerContact;
import com.sapwd.wdquote.domain.CustomerContact_;
import com.sapwd.wdquote.domain.Customer_;
import com.sapwd.wdquote.domain.Language_;
import com.sapwd.wdquote.repository.CustomerContactRepository;
import com.sapwd.wdquote.service.criteria.CustomerContactCriteria;
import com.sapwd.wdquote.service.dto.CustomerContactDTO;
import com.sapwd.wdquote.service.mapper.CustomerContactMapper;
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
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link CustomerContact} entities in the database.
 * The main input is a {@link CustomerContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerContactDTO} or a {@link Page} of {@link CustomerContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerContactQueryService extends QueryService<CustomerContact> {

    private final Logger log = LoggerFactory.getLogger(CustomerContactQueryService.class);

    private final CustomerContactRepository customerContactRepository;

    private final CustomerContactMapper customerContactMapper;

    public CustomerContactQueryService(CustomerContactRepository customerContactRepository, CustomerContactMapper customerContactMapper) {
        this.customerContactRepository = customerContactRepository;
        this.customerContactMapper = customerContactMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerContactDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerContactDTO> findByCriteria(CustomerContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerContact> specification = createSpecification(criteria);
        return customerContactMapper.toDto(customerContactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerContactDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerContactDTO> findByCriteria(CustomerContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerContact> specification = createSpecification(criteria);
        return customerContactRepository.findAll(specification, page).map(customerContactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerContact> specification = createSpecification(criteria);
        return customerContactRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerContactCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerContact> createSpecification(CustomerContactCriteria criteria) {
        Specification<CustomerContact> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), CustomerContact_.deleted)
        );

        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerContact_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), CustomerContact_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CustomerContact_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), CustomerContact_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerContact_.email));
            }
            if (criteria.getSalespersonCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSalespersonCode(), CustomerContact_.salespersonCode));
            }
            if (criteria.getIsDefaultContact() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefaultContact(), CustomerContact_.isDefaultContact));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(CustomerContact_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getCustomer() != null) {
                Specification<CustomerContact> orSpecification = Specification.where(null);
                orSpecification = getCustomerSpecification(criteria.getCustomer(), orSpecification);

                specification = specification.and(orSpecification);
            }
            if (criteria.getLanguageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLanguageId(),
                            root -> root.join(CustomerContact_.language, JoinType.LEFT).get(Language_.id)
                        )
                    );
            }

            if (criteria.getLanguage() != null) {
                Specification<CustomerContact> orSpecification = Specification.where(null);
                orSpecification = getLanguageSpecification(criteria.getLanguage(), orSpecification);

                specification = specification.and(orSpecification);
            }

            if (criteria.getGlobal() != null) {
                Specification<CustomerContact> orSpecification = Specification.where(null);

                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), CustomerContact_.code));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), CustomerContact_.name));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), CustomerContact_.phone));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), CustomerContact_.email));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), CustomerContact_.salespersonCode));
                orSpecification = getCustomerSpecification(criteria.getGlobal(), orSpecification);
                orSpecification = getLanguageSpecification(criteria.getGlobal(), orSpecification);

                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }

    private Specification<CustomerContact> getCustomerSpecification(StringFilter filter, Specification<CustomerContact> orSpecification) {
        orSpecification =
            orSpecification.or(buildSpecification(filter, root -> root.join(CustomerContact_.customer, JoinType.LEFT).get(Customer_.code)));
        orSpecification =
            orSpecification.or(buildSpecification(filter, root -> root.join(CustomerContact_.customer, JoinType.LEFT).get(Customer_.name)));
        return orSpecification;
    }

    private Specification<CustomerContact> getLanguageSpecification(StringFilter filter, Specification<CustomerContact> orSpecification) {
        orSpecification =
            orSpecification.or(buildSpecification(filter, root -> root.join(CustomerContact_.language, JoinType.LEFT).get(Language_.code)));
        orSpecification =
            orSpecification.or(buildSpecification(filter, root -> root.join(CustomerContact_.language, JoinType.LEFT).get(Language_.name)));
        return orSpecification;
    }
}
