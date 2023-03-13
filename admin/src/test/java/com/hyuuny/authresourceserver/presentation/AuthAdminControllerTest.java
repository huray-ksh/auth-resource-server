package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.UserService;
import com.hyuuny.authresourceserver.application.dto.AuthenticationDto;
import com.hyuuny.authresourceserver.application.dto.TokenDto;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.common.BaseIntegrationTests;
import com.hyuuny.authresourceserver.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.hyuuny.authresourceserver.Fixture.ADMIN_EMAIL;
import static com.hyuuny.authresourceserver.Fixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthAdminControllerTest extends BaseIntegrationTests {

    private final String AUTH_REQUEST_PATH = "/admin/auth";

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        deleteUsers();

        log.info("AuthAdminControllerTest.cache.clear()");
        redisRepository.clear();
    }

    @DisplayName("로그인에 성공하면 token 값을 반환한다.")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void login() throws Exception {
        UserDto.SignUpRequest signUpRequest = anUser()
                .email("tester@huray.net")
                .build();
        userService.signUp(signUpRequest);

        AuthenticationDto.LoginDto loginDto = AuthenticationDto.LoginDto.builder()
                .username(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        this.mockMvc.perform(post(AUTH_REQUEST_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(20000))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data.accessToken.accessToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshTokenExpiredAt").exists())
                .andExpect(jsonPath("data.accessToken.accessTokenExpiredAt").exists())
                .andExpect(jsonPath("data.userInfo.name").value(signUpRequest.getName()))
                .andExpect(jsonPath("data.userInfo.status").value(User.Status.ACTIVATION.name()))
                .andExpect(jsonPath("data.userInfo.gender").value(signUpRequest.getGender().name()))
                .andExpect(jsonPath("data.userInfo.username").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("data.userInfo.roleType").value(signUpRequest.getRoleType().name()))
        ;
    }

    @DisplayName("jwt 토큰 재발급")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void refreshToken() throws Exception {
        UserDto.SignUpRequest signUpRequest = anUser()
                .email("tester@huray.net")
                .build();
        userService.signUp(signUpRequest);

        AuthenticationDto.LoginDto loginDto = AuthenticationDto.LoginDto.builder()
                .username(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        String loginResult = this.mockMvc.perform(post(AUTH_REQUEST_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(20000))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data.accessToken.accessToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshTokenExpiredAt").exists())
                .andExpect(jsonPath("data.accessToken.accessTokenExpiredAt").exists())
                .andExpect(jsonPath("data.userInfo.name").value(signUpRequest.getName()))
                .andExpect(jsonPath("data.userInfo.status").value(User.Status.ACTIVATION.name()))
                .andExpect(jsonPath("data.userInfo.gender").value(signUpRequest.getGender().name()))
                .andExpect(jsonPath("data.userInfo.username").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("data.userInfo.roleType").value(signUpRequest.getRoleType().name()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthenticationDto.Data data = objectMapper.readValue(loginResult, AuthenticationDto.Data.class);
        AuthenticationDto.AccessToken existingAccessToken = data.getData().getAccessToken();


        TokenDto.RefreshTokenRequest refreshTokenRequest = new TokenDto.RefreshTokenRequest(
                existingAccessToken.getAccessToken(),
                existingAccessToken.getRefreshToken()
        );
        String refreshResult = this.mockMvc.perform(post(AUTH_REQUEST_PATH + "/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(refreshTokenRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(20000))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data.accessToken").exists())
                .andExpect(jsonPath("data.refreshToken").exists())
                .andExpect(jsonPath("data.refreshTokenExpiredAt").exists())
                .andExpect(jsonPath("data.accessTokenExpiredAt").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthenticationDto.AccessToken newAccessToken = objectMapper.readValue(refreshResult, AuthenticationDto.AccessToken.class);

        assertThat(newAccessToken.getAccessToken()).isNotEqualTo(existingAccessToken.getAccessToken());
        assertThat(newAccessToken.getRefreshToken()).isNotEqualTo(existingAccessToken.getRefreshToken());
        assertThat(newAccessToken.getAccessTokenExpiredAt()).isNotEqualTo(existingAccessToken.getAccessTokenExpiredAt());
        assertThat(newAccessToken.getRefreshTokenExpiredAt()).isNotEqualTo(existingAccessToken.getRefreshTokenExpiredAt());
    }

    @DisplayName("로그인에 성공하면 token 값을 반환한다.")
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void login() throws Exception {
        UserDto.SignUpRequest signUpRequest = anUser()
                .email("tester@huray.net")
                .build();
        userService.signUp(signUpRequest);

        AuthenticationDto.LoginDto loginDto = AuthenticationDto.LoginDto.builder()
                .username(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        this.mockMvc.perform(post(AUTH_REQUEST_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(20000))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data.accessToken.accessToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshToken").exists())
                .andExpect(jsonPath("data.accessToken.refreshTokenExpiredAt").exists())
                .andExpect(jsonPath("data.accessToken.accessTokenExpiredAt").exists())
                .andExpect(jsonPath("data.userInfo.name").value(signUpRequest.getName()))
                .andExpect(jsonPath("data.userInfo.status").value(User.Status.ACTIVATION.name()))
                .andExpect(jsonPath("data.userInfo.gender").value(signUpRequest.getGender().name()))
                .andExpect(jsonPath("data.userInfo.username").value(signUpRequest.getEmail()))
                .andExpect(jsonPath("data.userInfo.roleType").value(signUpRequest.getRoleType().name()))
        ;
    }

}