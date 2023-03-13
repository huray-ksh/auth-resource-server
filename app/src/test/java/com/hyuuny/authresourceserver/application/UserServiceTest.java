package com.hyuuny.authresourceserver.application;

import com.google.common.collect.Lists;
import com.hyuuny.authresourceserver.application.dto.PrivilegeDto;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.common.BaseIntegrationTests;
import com.hyuuny.authresourceserver.domain.entity.Privilege.PrivilegeType;
import com.hyuuny.authresourceserver.domain.entity.Role.RoleType;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.repository.PrivilegeRepository;
import com.hyuuny.authresourceserver.domain.repository.RoleRepository;
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.hyuuny.authresourceserver.Fixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserServiceTest extends BaseIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserService userService;

    private final String BASIC_STATUS = "ACTIVATION";

    @AfterEach
    void tearDown() {
        deleteUsers();
    }

    @DisplayName("회원 가입")
    @Test
    void signUp() {
        UserDto.SignUpRequest signUpRequest = anUser()
                .email("hyuuny@huray.net")
                .password("a123456!")
                .name("사용자")
                .mobilePhoneNumber("010-1234-1234")
                .gender(User.Gender.MALE)
                .roleType(RoleType.ROLE_SYS_USER)
                .privilegeTypes(Lists.newArrayList(
                        PrivilegeType.PRIVILEGE_USER_C,
                        PrivilegeType.PRIVILEGE_USER_R,
                        PrivilegeType.PRIVILEGE_USER_U,
                        PrivilegeType.PRIVILEGE_USER_D
                ))
                .build();

        // 회원 정보 검사
        UserDto.Response signUpUser = userService.signUp(signUpRequest);
        assertThat(signUpUser.getId()).isNotNull();
        assertThat(signUpUser.getUuid()).isNotNull();
        assertThat(signUpUser.getStatus().name()).isEqualTo(BASIC_STATUS);
        assertThat(signUpUser.getName()).isEqualTo(signUpRequest.getName());
        assertThat(signUpUser.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(signUpUser.getUserAccount().getUsername()).isEqualTo(signUpRequest.getEmail());
        assertThat(signUpUser.getUserAccount().getMobilePhoneNumber()).isEqualTo(signUpRequest.getMobilePhoneNumber());

        // 역할 검사
        assertThat(signUpUser.getRole().getType()).isEqualTo(RoleType.ROLE_SYS_USER);

        // 권한 검사
        assertThat(signUpUser.getRole().getPrivileges().size()).isEqualTo(4);
        List<PrivilegeType> signUpUserPrivilegeTypes = signUpUser.getRole().getPrivileges().stream()
                .map(PrivilegeDto.Response::getType)
                .toList();
        assertThat(signUpUserPrivilegeTypes).contains(PrivilegeType.PRIVILEGE_USER_R, PrivilegeType.PRIVILEGE_USER_C,
                PrivilegeType.PRIVILEGE_USER_U, PrivilegeType.PRIVILEGE_USER_D);
    }

}