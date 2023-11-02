package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.DeliveryZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeliveryZone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
    Page<DeliveryZone> findAllByDeletedIsFalse(Pageable pageable);
}
