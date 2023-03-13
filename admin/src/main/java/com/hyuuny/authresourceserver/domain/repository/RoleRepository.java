package com.hyuuny.authresourceserver.domain.repository;

import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.Role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByType(RoleType roleType);

}
