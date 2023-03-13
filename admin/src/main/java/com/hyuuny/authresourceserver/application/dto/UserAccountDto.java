package com.hyuuny.authresourceserver.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hyuuny.authresourceserver.domain.entity.UserAccount;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserAccountDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    public static class Response implements Serializable {

        private Long id;

        @NotEmpty
        private String username;

        @NotEmpty
        private String mobilePhoneNumber;

        private LocalDateTime lastLoginAt;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public Response(UserAccount entity) {
            this.id = entity.getId();
            this.username = entity.getUsername();
            this.mobilePhoneNumber = entity.getMobilePhoneNumber();
            this.lastLoginAt = entity.getLastLoginAt();
            this.createdAt = entity.getCreatedAt();
            this.updatedAt = entity.getUpdatedAt();
        }
    }

}
