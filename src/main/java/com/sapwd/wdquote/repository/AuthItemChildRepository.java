package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.AuthItemChild;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AuthItemChild entity.
 */
public interface AuthItemChildRepository extends JpaRepository<AuthItemChild, Long> {
    void deleteByParentId(Long id);

    Optional<AuthItemChild> findByParentIdAndChildId(Long parentId, Long childId);
}
