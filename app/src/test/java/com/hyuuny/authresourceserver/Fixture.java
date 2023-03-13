package com.hyuuny.authresourceserver;

import com.google.common.collect.Lists;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.User.Gender;

import java.util.List;

public class Fixture {

    public static final String ADMIN_EMAIL = "basicAdmin@huray.net";

    public static final String ADMIN_PASSWORD = "b123456!!";

    public static final String USER_EMAIL = "basicUser@huray.net";

    public static final String USER_PASSWORD = "a123455A!";

    public static UserDto.SignUpRequest.SignUpRequestBuilder anAdmin() {
        return UserDto.SignUpRequest.builder()
                .email(ADMIN_EMAIL)
                .password(ADMIN_PASSWORD)
                .name("관리자")
                .mobilePhoneNumber("010-1242-2312")
                .gender(Gender.FEMALE)
                .roleType(Role.RoleType.ROLE_SYS_ADMIN)
                .privilegeTypes(Lists.newArrayList(
                        Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION_OP,
                        Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION,
                        Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION_D,
                        Privilege.PrivilegeType.PRIVILEGE_USER_R,
                        Privilege.PrivilegeType.PRIVILEGE_USER_U,
                        Privilege.PrivilegeType.PRIVILEGE_USER_D
                ));
    }

    public static UserDto.SignUpRequest.SignUpRequestBuilder anUser() {
        return UserDto.SignUpRequest.builder()
                .email("hyuuny@huray.net")
                .password("a123456!")
                .name("사용자")
                .mobilePhoneNumber("010-1234-1234")
                .gender(Gender.MALE)
                .roleType(Role.RoleType.ROLE_SYS_USER)
                .privilegeTypes(Lists.newArrayList(
                        Privilege.PrivilegeType.PRIVILEGE_USER_R,
                        Privilege.PrivilegeType.PRIVILEGE_USER_C,
                        Privilege.PrivilegeType.PRIVILEGE_USER_U,
                        Privilege.PrivilegeType.PRIVILEGE_USER_D
                ));
    }

    public static List<Role> aRoles() {
        return Lists.newArrayList(
                aRole().build(),
                aRole()
                        .name("role.name.system.admin")
                        .type(Role.RoleType.ROLE_SYS_ADMIN)
                        .description("role.description.system.admin")
                        .build(),
                aRole()
                        .name("role.name.system.manager")
                        .type(Role.RoleType.ROLE_SYS_MANAGER)
                        .description("role.description.system.manager")
                        .build()
        );
    }

    public static List<Privilege> anAdminPrivileges() {
        return Lists.newArrayList(
                aPrivilege()
                        .name("privilege.organization")
                        .type(Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION)
                        .description("privilege.description.organization")
                        .build(),
                aPrivilege()
                        .name("privilege.organization.op")
                        .type(Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION_OP)
                        .description("privilege.description.organization.op")
                        .build(),
                aPrivilege()
                        .name("privilege.organization.d")
                        .type(Privilege.PrivilegeType.PRIVILEGE_ORGANIZATION_D)
                        .description("privilege.description.organization.d")
                        .build(),
                aPrivilege().build(),
                aPrivilege()
                        .name("privilege.name.user.c")
                        .type(Privilege.PrivilegeType.PRIVILEGE_USER_C)
                        .description("privilege.description.user.c")
                        .build(),
                aPrivilege()
                        .name("privilege.name.user.u")
                        .type(Privilege.PrivilegeType.PRIVILEGE_USER_U)
                        .description("privilege.description.user.u")
                        .build(),
                aPrivilege()
                        .name("privilege.name.user.d")
                        .type(Privilege.PrivilegeType.PRIVILEGE_USER_D)
                        .description("privilege.description.user.d")
                        .build()
        );
    }

    public static Role.RoleBuilder aRole() {
        return Role.builder()
                .name("role.name.user")
                .type(Role.RoleType.ROLE_SYS_USER)
                .description("role.description.user");
    }

    public static Privilege.PrivilegeBuilder aPrivilege() {
        return Privilege.builder()
                .name("privilege.name.user.r")
                .type(Privilege.PrivilegeType.PRIVILEGE_USER_R)
                .description("privilege.description.user.r");
    }

}
