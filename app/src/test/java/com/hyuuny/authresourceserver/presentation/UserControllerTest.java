package com.hyuuny.authresourceserver.presentation;

import com.google.common.collect.Lists;
import com.hyuuny.authresourceserver.application.UserService;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.common.BaseIntegrationTests;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.infrastructure.create.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.hyuuny.authresourceserver.Fixture.ADMIN_EMAIL;
import static com.hyuuny.authresourceserver.Fixture.anUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class UserControllerTest extends BaseIntegrationTests {

    private final String USER_REQUEST_PATH = "/users";

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        deleteUsers();

        log.info("UserControllerTest.cache.clear()");
        redisRepository.clear();
    }

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {
        UserDto.SignUpRequest signUpRequest = anUser()
                .email("hyuuny@huray.net")
                .password("a123456!")
                .name("사용자")
                .mobilePhoneNumber("010-1234-1234")
                .gender(User.Gender.MALE)
                .roleType(Role.RoleType.ROLE_SYS_USER)
                .privilegeTypes(Lists.newArrayList(
                        Privilege.PrivilegeType.PRIVILEGE_USER_C,
                        Privilege.PrivilegeType.PRIVILEGE_USER_R,
                        Privilege.PrivilegeType.PRIVILEGE_USER_U,
                        Privilege.PrivilegeType.PRIVILEGE_USER_D
                ))
                .build();

        this.mockMvc.perform(post(USER_REQUEST_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(signUpRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @DisplayName("회원 상세 조회")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void getUser() throws Exception {
        UserDto.SignUpRequest signUpRequest = anUser().build();
        UserDto.Response signUpUser = userService.signUp(signUpRequest);

        this.mockMvc.perform(get(USER_REQUEST_PATH + "/{id}", signUpUser.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(signUpRequest.getEmail(), signUpRequest.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}