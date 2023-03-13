package com.hyuuny.authresourceserver.common;

public class AuthResourceAppConstant extends AuthCoreConstant {

    // Crate -----------------------------------------------------------------------------------------------------------
    public static final String CRATE_AUTH_RESOURCE_APP_KEY_DEFAULT = CRATE_AUTH_RESOURCE_KEY_DEFAULT + "%s:app:";     // %s 에 해당 서비스 unique 명을 치환
    public static final String CRATE_AUTH_RESOURCE_APP_KEY_SYS_ADMIN = CRATE_AUTH_RESOURCE_APP_KEY_DEFAULT + "admin:";

    // Admin Role ------------------------------------------------------------------------------------------------------
    public static final String ROLE_SERVICE_SYS_ADMIN_TYPE = "ROLE_SYS_ADMIN";

}
