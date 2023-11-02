package com.sapwd.wdquote.repository;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.service.projection.AuthTreeProj;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the AuthItem entity.
 */
public interface AuthItemRepository extends JpaRepository<AuthItem, Long>, JpaSpecificationExecutor<AuthItem> {
    /**
     * Return the Role by ID.
     *
     * @param id The entity ID.
     * @return The role.
     */
    @Query(
        "SELECT authItem FROM AuthItem authItem " +
        "WHERE authItem.type = com.sapwd.wdquote.security.AuthorityType.ROLE " +
        "AND authItem.id = :id"
    )
    Optional<AuthItem> findRole(@Param("id") Long id);

    /**
     * Return the Resource by ID.
     *
     * @param id The entity ID.
     * @return The resource.
     */
    @Query(
        "SELECT authItem FROM AuthItem authItem " +
        "WHERE authItem.type = com.sapwd.wdquote.security.AuthorityType.RESOURCE " +
        "AND authItem.id = :id"
    )
    Optional<AuthItem> findResource(@Param("id") Long id);

    /**
     * Return the Action by ID.
     *
     * @param id The entity ID.
     * @return The action.
     */
    @Query(
        "SELECT authItem FROM AuthItem authItem " +
        "WHERE authItem.type = com.sapwd.wdquote.security.AuthorityType.ACTION " +
        "AND authItem.id = :id"
    )
    Optional<AuthItem> findAction(@Param("id") Long id);

    @Query(
        "SELECT authItemChild.child FROM AuthItemChild authItemChild " +
        "INNER JOIN authItemChild.child child " +
        "INNER JOIN authItemChild.parent parent " +
        "WHERE parent.id = :id"
    )
    List<AuthItem> findAllChildren(@Param("id") Long id);

    @Query("SELECT authItem FROM AuthItem authItem " + "LEFT JOIN FETCH authItem.children children " + "WHERE authItem.name = :name")
    Optional<AuthItem> findAuthItemByName(@Param("name") String name);

    @Query(
        nativeQuery = true,
        value = "WITH RECURSIVE parents AS (" +
        "SELECT parent.*, child.parent_id, child.child_id " +
        "FROM auth_item parent LEFT JOIN auth_item_child child ON child.parent_id = parent.id " +
        "WHERE parent.name = :name " +
        "UNION " +
        "SELECT parent.*, child.parent_id, child.child_id " +
        "FROM auth_item parent LEFT JOIN auth_item_child child ON child.parent_id = parent.id " +
        "   INNER JOIN parents s ON s.child_id = parent.id " +
        "WHERE parent.item_type = :type) " +
        "SELECT * FROM parents"
    )
    HashSet<AuthItem> findChildrenTreeByNameFilterByType(@Param("name") String name, @Param("type") Integer type);

    @Query(
        nativeQuery = true,
        value = "WITH RECURSIVE parents AS (" +
        "SELECT c.child_id, dp.name AS child_name, c.parent_id, p.name AS parent_name, p.item_type AS parent_type " +
        "FROM auth_item_child c " +
        "   INNER JOIN auth_item p ON p.id = c.parent_id " +
        "   INNER JOIN auth_item dp ON dp.id = c.child_id " +
        "UNION " +
        "SELECT p.child_id, dp.name AS child_name, p.parent_id, t.name AS parent_name, t.item_type AS parent_type " +
        "FROM auth_item_child p " +
        "   INNER JOIN auth_item t ON t.id = p.parent_id " +
        "   INNER JOIN auth_item dp ON dp.id = p.child_id INNER JOIN parents s ON s.parent_id = p.child_id " +
        ") " +
        "SELECT child_id AS childId, child_name AS childName, " +
        "   parent_id AS parentId, parent_name AS parentName, parent_type AS parentType " +
        "FROM parents"
    )
    List<AuthTreeProj> findParentTreeById(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "WITH RECURSIVE parents AS (" +
        "SELECT c.child_id, dp.name AS child_name, c.parent_id, p.name AS parent_name, p.item_type AS parent_type " +
        "FROM auth_item_child c " +
        "   INNER JOIN auth_item p ON p.id = c.parent_id " +
        "   INNER JOIN auth_item dp ON dp.id = c.child_id " +
        "WHERE child_id IN (:ids) " +
        "UNION " +
        "SELECT p.child_id, dp.name AS child_name, p.parent_id, t.name AS parent_name, t.item_type AS parent_type " +
        "FROM auth_item_child p " +
        "   INNER JOIN auth_item t ON t.id = p.parent_id " +
        "   INNER JOIN auth_item dp ON dp.id = p.child_id INNER JOIN parents s ON s.parent_id = p.child_id " +
        ") " +
        "SELECT child_id AS childId, child_name AS childName, " +
        "   parent_id AS parentId, parent_name AS parentName, parent_type AS parentType " +
        "FROM parents"
    )
    List<AuthTreeProj> findParentTreeByIds(@Param("ids") List<Long> ids);
}
