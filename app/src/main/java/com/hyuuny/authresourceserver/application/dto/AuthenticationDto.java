package com.hyuuny.authresourceserver.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hyuuny.authresourceserver.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuthenticationDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {

        private String username;

        private String password;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_NULL)
    public static class Data implements Serializable {

        private UserWithToken data;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @JsonInclude(Include.NON_NULL)
    public static class UserWithToken implements Serializable {

        private String additionalMessage;

        private AccessToken accessToken;

        private UserInfo userInfo;

        public UserWithToken(AccessToken accessToken, UserInfo userInfo) {
            this(null, accessToken, userInfo);
        }

        public UserWithToken(String additionalMessage, AccessToken accessToken, UserInfo userInfo) {
            this.additionalMessage = additionalMessage;
            this.accessToken = accessToken;
            this.userInfo = userInfo;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessToken implements Serializable {

        private String accessToken;

        private String refreshToken;

        private LocalDateTime accessTokenExpiredAt;

        private LocalDateTime refreshTokenExpiredAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo implements Serializable {

        private Long id;

        private String name;

        private User.Status status;

        private User.Gender gender;

        private String username;

        private String roleType;

        public UserInfo(UserAdapter adapter) {
            this.id = adapter.getUserId();
            this.name = adapter.getName();
            this.status = adapter.getStatus();
            this.gender = adapter.getGender();
            this.username = adapter.getUsername();
            this.roleType = adapter.getRoleType();
        }

    }

}
