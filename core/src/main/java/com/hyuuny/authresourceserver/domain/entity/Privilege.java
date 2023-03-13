package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.common.jpa.domain.NonCreatedByAuditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Privilege extends NonCreatedByAuditable {

    @Getter
    @RequiredArgsConstructor
    public enum PrivilegeType {
        PRIVILEGE_ORGANIZATION("조직을 조회할 수 있는 권한"),
        PRIVILEGE_ORGANIZATION_OP("조직을 등록하거나 수정할 수 있는 권한"),
        PRIVILEGE_ORGANIZATION_D("조직을 삭제할 수 있는 권한"),
        PRIVILEGE_USER_R("회원이 조회할 수 있는 권한"),

        PRIVILEGE_USER_C("회원이 등록할 수 있는 권한"),
        PRIVILEGE_USER_U("회원이 수정할 수 있는 권한"),
        PRIVILEGE_USER_D("회원이 삭제할 수 있는 권한");

        private final String title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PrivilegeType type;

    private String description;

    @OneToMany(mappedBy = "privilege", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolePrivilege> roles;

}
