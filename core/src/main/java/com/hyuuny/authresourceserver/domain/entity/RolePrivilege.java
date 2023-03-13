package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.common.jpa.domain.NonCreatedByAuditable;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class RolePrivilege extends NonCreatedByAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Privilege privilege;

    public void assignRole(Role role) {
        if (this.role != null) {
            this.role.getRolePrivileges().remove(this);
        }
        this.role = role;
        this.role.getRolePrivileges().add(this);
    }

    public Privilege.PrivilegeType getPrivilegeType() {
        return this.privilege.getType();
    }

}
