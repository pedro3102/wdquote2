package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.domain.AuthItemChild;
import com.sapwd.wdquote.repository.AuthItemChildRepository;
import com.sapwd.wdquote.service.AuthItemChildService;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import com.sapwd.wdquote.service.mapper.AuthItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AuthItemChild.
 */
@Service
@Transactional
public class AuthItemChildServiceImpl implements AuthItemChildService {

    private final Logger log = LoggerFactory.getLogger(AuthItemChildServiceImpl.class);
    private final AuthItemChildRepository authItemChildRepository;
    private final AuthItemMapper authItemMapper;

    public AuthItemChildServiceImpl(AuthItemChildRepository authItemChildRepository, AuthItemMapper authItemMapper) {
        this.authItemChildRepository = authItemChildRepository;
        this.authItemMapper = authItemMapper;
    }

    /**
     * Toggle action status for a parent by ID.
     *
     * @param parentId  The parent ID.
     * @param actionDTO The actionDTO (AuthItemDTO).
     * @return The actionDTO toggled
     */
    @Override
    public AuthItemDTO toggleActionStatus(Long parentId, AuthItemDTO actionDTO) {
        Optional<AuthItemChild> optional = authItemChildRepository.findByParentIdAndChildId(parentId, actionDTO.getId());
        if (optional.isPresent()) { // Remove if present
            authItemChildRepository.delete(optional.get());
        } else { // Add if no present
            var authItemChild = new AuthItemChild();
            var resource = new AuthItem();
            resource.setId(parentId);
            authItemChild.setParent(resource);
            authItemChild.setChild(authItemMapper.toEntity(actionDTO));
            authItemChildRepository.save(authItemChild);
        }
        return actionDTO;
    }
}
