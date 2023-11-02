package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long>, JpaSpecificationExecutor<CustomerContact> {}
