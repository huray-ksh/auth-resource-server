package com.hyuuny.authresourceserver.domain.entity;

import com.google.common.collect.Sets;
import com.hyuuny.authresourceserver.common.jpa.domain.NonCreatedByAuditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

import java.util.Set;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Role extends NonCreatedByAuditable {

    @Getter
    @RequiredArgsConstructor
    public enum RoleType {
        ROLE_SYS_ADMIN("SYS_ADMIN"),
        ROLE_SYS_MANAGER("SYS_MANAGER"),
        ROLE_SYS_DEV_MANAGER("SYS_DEV_MANAGER"),
        ROLE_SYS_SERVICE_MANAGER("SYS_SERVICE_MANAGER"),
        ROLE_SYS_USER("SYS_USER");

        private final String title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType type;

    @Column(nullable = false)
    private String description;

    @Default
    @OrderBy("privilege.id ASC")
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<RolePrivilege> rolePrivileges = Sets.newHashSet();

    @Default
    @OneToMany(mappedBy = "role")
    private Set<User> users = Sets.newHashSet();

    public void addUser(User user) {
        user.assignRole(this);
    }

    public void addPrivilege(Privilege privilege) {
        RolePrivilege rolePrivilege = RolePrivilege.builder()
                .role(this)
                .privilege(privilege)
                .build();
        rolePrivilege.assignRole(this);
    }

    public String toType() {
        return this.type.name();
    }

}
