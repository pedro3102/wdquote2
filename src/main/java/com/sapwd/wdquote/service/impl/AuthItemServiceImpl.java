package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.domain.AuthItemChild;
import com.sapwd.wdquote.repository.AuthItemRepository;
import com.sapwd.wdquote.security.AuthorityType;
import com.sapwd.wdquote.service.AuthItemService;
import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import com.sapwd.wdquote.service.mapper.AuthItemMapper;
import com.sapwd.wdquote.service.projection.AuthTreeProj;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AuthItem.
 */
@Service
@Transactional
public class AuthItemServiceImpl implements AuthItemService {

    private final Logger log = LoggerFactory.getLogger(AuthItemServiceImpl.class);
    private final AuthItemRepository authItemRepository;
    private final AuthItemMapper authItemMapper;

    public AuthItemServiceImpl(AuthItemRepository authItemRepository, AuthItemMapper authItemMapper) {
        this.authItemRepository = authItemRepository;
        this.authItemMapper = authItemMapper;
    }

    /**
     * Save a AuthItem.
     *
     * @param authItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public Optional<AuthItemDTO> partialUpdate(AuthItemDTO authItemDTO) {
        log.debug("Request to save AuthItem : {}", authItemDTO);
        return authItemRepository
            .findById(authItemDTO.getId())
            .map(existingAuthItem -> {
                authItemMapper.partialUpdate(existingAuthItem, authItemDTO);
                return existingAuthItem;
            })
            .map(authItemRepository::save)
            .map(authItemMapper::toDto);
    }

    /**
     * Delete the  authItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AuthItem : {}", id);
        authItemRepository
            .findRole(id)
            .ifPresent(authItem -> {
                authItem.setDeleted(true);
                authItemRepository.save(authItem);
            });
    }

    /**
     * Get Action authorization, This will find all Action roles recursive by Action name.
     *
     * @param action Action name.
     * @return ActionDTO Authorization tree.
     */
    @Override
    public Optional<ActionDTO> actionAuthorization(String action) {
        log.debug("Request to get Action authorizations");
        var authItem = authItemRepository.findAuthItemByName(action);
        if (authItem.isPresent()) {
            ActionDTO actionDTO = new ActionDTO();
            actionDTO.setAction(authItem.get().getName());
            var roles = authItemRepository
                .findParentTreeById(authItem.get().getId())
                .stream()
                .filter(x -> x.getParentType().equals(AuthorityType.ROLE))
                .map(AuthTreeProj::getParentName)
                .collect(Collectors.toCollection(HashSet::new));
            actionDTO.setRoles(roles);
            return Optional.of(actionDTO);
        }
        return Optional.empty();
    }

    /**
     * Get Resource authorization, This will find all Resource Actions and for each Action,
     * get the roles recursive.
     *
     * @param resourceName Resource name.
     * @return ResourceDTO Authorization tree.
     */
    @Override
    public Optional<ResourceDTO> resourceAuthorization(String resourceName) {
        log.debug("Request to get Resource authorizations");
        if (!resourceName.isEmpty()) {
            var authItems = authItemRepository.findChildrenTreeByNameFilterByType(resourceName, AuthorityType.ACTION);
            Optional<AuthItem> resourceOptional = authItems.stream().filter(item -> item.getName().equals(resourceName)).findFirst();
            if (resourceOptional.isPresent()) {
                ResourceDTO resourceDTO = new ResourceDTO();
                resourceDTO.setName(resourceName);
                resourceDTO.setActions(getChildActionDTOs(resourceOptional.get()));
                return Optional.of(resourceDTO);
            }
        }
        return Optional.empty();
    }

    /**
     * Util function to find all Parent Roles recursive for an specific Resource.
     *
     * @param resource AuthItem(Resource) to get it's parent actions roles.
     * @return ActionDTO with all parent roles.
     */
    private HashMap<String, ActionDTO> getChildActionDTOs(AuthItem resource) {
        //Convert children to map
        Map<Long, AuthItem> map = convertChildrenToMap(resource);
        return getActionDTOs(map);
    }

    /**
     * Util function to find all Parent Roles recursive for an specific Resource.
     *
     * @param map Actions map.
     * @return ActionDTO with all parent roles.
     */
    private HashMap<String, ActionDTO> getActionDTOs(Map<Long, AuthItem> map) {
        HashMap<String, ActionDTO> actions = new HashMap<>();
        //Convert children to map
        if (!map.isEmpty()) {
            //Get map keys
            List<Long> ids = getMapKeys(map);
            //Get parent tree by ids
            List<AuthTreeProj> authTree = authItemRepository.findParentTreeByIds(ids);
            if (ids != null) ids.forEach(id -> {
                ActionDTO actionDTO = new ActionDTO();
                actionDTO.setAction(map.get(id).getName());
                //Filter direct parents by action id
                List<AuthTreeProj> directParents = filterParentsByActionId(authTree, id);
                //Init with direct parent roles
                HashSet<String> roles = initRolesFromDirectParents(directParents);
                //Get direct parent resources ids
                List<Long> parentResourcesIds = getParentResourcesIds(directParents);
                //Attach resources parents roles
                roles.addAll(getParentalResourceRoles(parentResourcesIds, authTree));
                //Set the Set of Roles
                actionDTO.getRoles().addAll(roles);
                actions.put(actionDTO.getAction(), actionDTO);
            });
        }
        return actions;
    }

    /**
     * Get parent resource roles.
     *
     * @param parentResourcesIds Parent resource ids
     * @param authTree           Resource auth tree.
     */
    private HashSet<String> getParentalResourceRoles(List<Long> parentResourcesIds, List<AuthTreeProj> authTree) {
        HashSet<String> roles = new HashSet<>();
        if (!parentResourcesIds.isEmpty()) {
            parentResourcesIds
                .stream()
                .map(child ->
                    authTree
                        .stream()
                        .filter(x -> x.getChildId().equals(child) && x.getParentType().equals(AuthorityType.ROLE))
                        .map(AuthTreeProj::getParentName)
                        .collect(Collectors.toCollection(HashSet::new))
                )
                .forEach(roles::addAll);
        }
        return roles;
    }

    /**
     * Get parents resources ids.
     *
     * @param directParents List of direct parents resources.
     * @return List of parents ids
     */
    private List<Long> getParentResourcesIds(List<AuthTreeProj> directParents) {
        List<Long> result = new ArrayList<>();
        if (!directParents.isEmpty()) {
            result =
                directParents
                    .stream()
                    .filter(x -> x.getParentType().equals(AuthorityType.RESOURCE))
                    .map(AuthTreeProj::getParentId)
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Init result roles with direct parent roles.
     *
     * @param directParents Direct parents filtered by the action.
     * @return Set of roles.
     */
    private HashSet<String> initRolesFromDirectParents(List<AuthTreeProj> directParents) {
        HashSet<String> result = new HashSet<>();
        if (!directParents.isEmpty()) {
            result =
                directParents
                    .stream()
                    .filter(x -> x.getParentType().equals(AuthorityType.ROLE))
                    .map(AuthTreeProj::getParentName)
                    .collect(Collectors.toCollection(HashSet::new));
        }
        return result;
    }

    /**
     * Filter direct parents by action id.
     *
     * @param directParents List of direct parents.
     * @param id            Action id
     * @return Direct parents filter by teh action id.
     */
    private List<AuthTreeProj> filterParentsByActionId(List<AuthTreeProj> directParents, Long id) {
        List<AuthTreeProj> result = new ArrayList<>();
        if (!directParents.isEmpty()) {
            result = directParents.stream().filter(x -> x.getChildId().equals(id)).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Get Map keys.
     *
     * @param map Map to get keys
     * @return List of map keys
     */
    private List<Long> getMapKeys(Map<Long, AuthItem> map) {
        List<Long> ids = new ArrayList<>(map.keySet());
        return ids.isEmpty() ? null : ids;
    }

    /**
     * Convert resource children to map.
     *
     * @param resource AuthItem
     * @return Map of children
     */
    private Map<Long, AuthItem> convertChildrenToMap(AuthItem resource) {
        if (resource != null) {
            return resource
                .getChildren()
                .stream()
                .filter(child -> child.getChild().getType().equals(AuthorityType.ACTION))
                .collect(Collectors.toMap(child -> child.getChild().getId(), AuthItemChild::getChild));
        } else {
            return new HashMap<>();
        }
    }
}
