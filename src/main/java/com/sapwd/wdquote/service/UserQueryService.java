package com.sapwd.wdquote.service;

import com.sapwd.wdquote.domain.AuthItem_;
import com.sapwd.wdquote.domain.User;
import com.sapwd.wdquote.domain.User_;
import com.sapwd.wdquote.repository.UserRepository;
import com.sapwd.wdquote.service.criteria.UserCriteria;
import com.sapwd.wdquote.service.dto.UserDTO;
import com.sapwd.wdquote.service.mapper.UserMapper;
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
 * Service for executing complex queries for {@link User} entities in the database.
 * The main input is a {@link UserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDTO} or a {@link Page} of {@link UserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link List} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userMapper.toDto(userRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.findAll(specification, page).map(userMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.count(specification);
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(buildSpecification(new BooleanFilter().setEquals(false), User_.deleted));
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), User_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), User_.login));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), User_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), User_.email));
            }
            if (criteria.getAuthorities() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAuthorities(),
                            root -> root.join(User_.authorities, JoinType.LEFT).get(AuthItem_.name)
                        )
                    );
            }
            if (criteria.getActivated() != null) {
                specification =
                    specification.and(
                        buildSpecification(new BooleanFilter().setEquals(criteria.getActivated().getEquals()), User_.activated)
                    );
            }
            if (criteria.getGlobal() != null) {
                Specification<User> orSpecification = Specification.where(null);
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), User_.login));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), User_.firstName));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), User_.lastName));
                orSpecification = orSpecification.or(buildStringSpecification(criteria.getGlobal(), User_.email));
                specification = specification.and(orSpecification);
            }
        }
        return specification;
    }
}
