package com.hyuuny.authresourceserver.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.entity.User.Gender;
import com.hyuuny.authresourceserver.domain.entity.User.Status;
import com.hyuuny.authresourceserver.domain.entity.UserAccount;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Builder.Default;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserDto {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SignUpRequest {

        @NotEmpty
        private String email;

        private String password;

        @NotEmpty
        private String name;

        @NotEmpty
        private String mobilePhoneNumber;

        @NotNull
        private Gender gender;

        @NotEmpty
        private Role.RoleType roleType;

        @Default
        @NotEmpty
        private List<Privilege.PrivilegeType> privilegeTypes = Lists.newArrayList();

        public User toEntity() {
            return User.builder()
                    .name(this.name)
                    .gender(this.gender)
                    .userAccount(UserAccount.builder()
                            .username(this.email)
                            .password(this.password)
                            .mobilePhoneNumber(this.mobilePhoneNumber)
                            .lastLoginAt(LocalDateTime.now())
                            .build())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    public static class Response implements Serializable {

        private Long id;

        @NotEmpty
        private UUID uuid;

        @NotEmpty
        private Status status;

        @NotEmpty
        private String name;

        @NotEmpty
        private Gender gender;

        @NotEmpty
        private UserAccountDto.Response userAccount;

        @NotEmpty
        private RoleDto.Response role;

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createdAt;

        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updatedAt;

        public Response(User entity) {
            this.id = entity.getId();
            this.uuid = entity.getUuid();
            this.status = entity.getStatus();
            this.name = entity.getName();
            this.gender = entity.getGender();
            this.userAccount = new UserAccountDto.Response(entity.getUserAccount());
            this.role = new RoleDto.Response(entity.getRole());
            this.createdAt = entity.getCreatedAt();
            this.updatedAt = entity.getUpdatedAt();
        }
    }

}
