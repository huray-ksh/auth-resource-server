package com.hyuuny.authresourceserver.domain.repository;

import com.hyuuny.authresourceserver.domain.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    List<Privilege> findAllByTypeIn(List<Privilege.PrivilegeType> privilegeTypes);

}
