package com.hyuuny.authresourceserver.application.dto;

import com.google.common.collect.Lists;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.entity.User.Gender;
import com.hyuuny.authresourceserver.domain.entity.User.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAdapter implements UserDetails {

    private User user;

    private List<Privilege.PrivilegeType> privilegeTypes;

    public UserAdapter(User user, List<Privilege.PrivilegeType> privilegeTypes) {
        this.user = user;
        this.privilegeTypes = privilegeTypes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = Lists.newArrayList();
        authorities.add(new SimpleGrantedAuthority(this.user.getRole().toType()));
        this.privilegeTypes
                .forEach(privilegeType -> authorities.add(new SimpleGrantedAuthority(privilegeType.name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getUserAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserAccount().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getStatus().isActiveUser();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getStatus().isActiveUser();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getStatus().isActiveUser();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getStatus().isActiveUser();
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public String getName() {
        return this.user.getName();
    }

    public Status getStatus() {
        return this.user.getStatus();
    }

    public Gender getGender() {
        return this.user.getGender();
    }

    public String getRoleType() {
        return this.user.getRole().toType();
    }

}

