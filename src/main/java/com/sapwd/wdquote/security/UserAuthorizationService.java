package com.sapwd.wdquote.security;

import com.sapwd.wdquote.domain.User;
import com.sapwd.wdquote.repository.UserRepository;
import com.sapwd.wdquote.service.AuthorizationService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationService {

    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    public UserAuthorizationService(AuthorizationService authorizationService, UserRepository userRepository) {
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }

    public Optional<User> getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    /**
     * Check if the current user has access to get the user info.
     *
     * @return If it has access to get user info.
     */
    public boolean hasAccessToManage() {
        boolean isSystem = isSystem();
        boolean isAdmin = isAdmin();
        // This case could be when new user action userId is null
        return isSystem || isAdmin;
    }

    /**
     * Check if the current user is a ROLE_SYSTEM.
     *
     * @return If it has access as ROLE_SYSTEM.
     */
    public boolean isSystem() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.SYSTEM);
    }

    /**
     * Check if the current user is a ROLE_ADMIN.
     *
     * @return If it has access as ROLE_ADMIN.
     */
    public boolean isAdmin() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN);
    }
}
