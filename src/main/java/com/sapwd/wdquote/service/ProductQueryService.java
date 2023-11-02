package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.*;
import com.sapwd.wdquote.repository.ProductRepository;
import com.sapwd.wdquote.service.criteria.ProductCriteria;
import com.sapwd.wdquote.service.dto.ProductDTO;
import com.sapwd.wdquote.service.mapper.ProductMapper;
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

import java.util.List;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteriaBasic(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDtoBasic);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(
            buildSpecification(new BooleanFilter().setEquals(false), Product_.deleted)
        );
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Product_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getCodeDescription() != null) {
                Specification<Product> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getCodeDescription(), Product_.code));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getCodeDescription(), Product_.description));

                specification = specification.and(orSpecification);
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Product_.weight));
            }
            if (criteria.getPicture() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicture(), Product_.picture));
            }
            if (criteria.getUomId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUomId(), root -> root.join(Product_.uom, JoinType.LEFT).get(UnitOfMeasure_.id))
                    );
            }

            if (criteria.getUom() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUom(),
                            root -> root.join(Product_.uom, JoinType.LEFT).get(UnitOfMeasure_.abbreviation)
                        )
                    );
            }
            if (criteria.getUomWeightId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUomWeightId(),
                            root -> root.join(Product_.uomWeight, JoinType.LEFT).get(UnitOfMeasure_.id)
                        )
                    );
            }

            if (criteria.getUomWeight() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUomWeight(),
                            root -> root.join(Product_.uomWeight, JoinType.LEFT).get(UnitOfMeasure_.abbreviation)
                        )
                    );
            }

            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(Product_.category, JoinType.LEFT).get(ProductCategory_.id)
                        )
                    );
            }

            if (criteria.getCategory() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategory(),
                            root -> root.join(Product_.category, JoinType.LEFT).get(ProductCategory_.name)
                        )
                    );
            }
            if (criteria.getSystemModelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemModelId(),
                            root -> root.join(Product_.systemModels, JoinType.LEFT).get(SystemModel_.id)
                        )
                    );
            }
            if (criteria.getSystemModels() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemModels(),
                            root -> root.join(Product_.systemModels, JoinType.LEFT).get(SystemModel_.name)
                        )
                    );
            }
            if (criteria.getGlobal() != null) {
                Specification<Product> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Product_.code));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), Product_.description));
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Product_.category, JoinType.LEFT).get(ProductCategory_.name)
                        )
                    );
                orSpecification =
                    orSpecification.or(
                        buildSpecification(
                            criteria.getGlobal(),
                            root -> root.join(Product_.systemModels, JoinType.LEFT).get(SystemModel_.name)
                        )
                    );

                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }
}
