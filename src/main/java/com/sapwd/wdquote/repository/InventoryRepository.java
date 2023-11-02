package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.Inventory;
import com.sapwd.wdquote.domain.Location;
import com.sapwd.wdquote.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    List<Inventory> findAllByProductAndLocationAndDeletedIsFalseOrderByLastModifiedDateDesc(Product product, Location location);
}
