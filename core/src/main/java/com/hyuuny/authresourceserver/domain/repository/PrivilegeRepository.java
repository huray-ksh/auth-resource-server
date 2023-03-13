package com.hyuuny.authresourceserver.domain.repository;

import com.hyuuny.authresourceserver.domain.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.hyuuny.authresourceserver.domain.entity.Privilege.PrivilegeType;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    List<Privilege> findAllByTypeIn(List<PrivilegeType> privilegeTypes);

}
