package com.hyuuny.authresourceserver.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@PreAuthorize("isAuthenticated() and (hasRole('SYS_ADMIN') or @authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R'))")
//@PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER') or @authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R'))")
public @interface CustomPrivilege {

    String userRole() default "";

    String userPrivilege() default "";

    String userScope() default "";


}
