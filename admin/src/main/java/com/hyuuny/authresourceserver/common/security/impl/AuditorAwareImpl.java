package com.hyuuny.authresourceserver.common.security.impl;

import com.hyuuny.authresourceserver.application.dto.UserAdapter;
import com.hyuuny.authresourceserver.common.abstraction.AbstractAuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl extends AbstractAuditorAware {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> !AnonymousAuthenticationToken.class
                        .isAssignableFrom(authentication.getClass()))
                .map(Authentication::getPrincipal)
                .filter(principal -> UserAdapter.class
                        .isAssignableFrom(principal.getClass()))
                .map(UserAdapter.class::cast)
                .map(UserAdapter::getName);
    }

}
