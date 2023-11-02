package com.sapwd.wdquote.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final long ROLE_SYSTEM_ID = 1L;
    public static final long ROLE_ADMIN_ID = 2L;
    public static final long ROLE_USER_ID = 3L;

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String SYSTEM = "ROLE_SYSTEM";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
