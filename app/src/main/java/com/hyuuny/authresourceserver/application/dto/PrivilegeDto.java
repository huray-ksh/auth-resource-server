package com.hyuuny.authresourceserver.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Privilege.PrivilegeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PrivilegeDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    public static class Response implements Serializable {

        private Long id;

        private String name;

        private PrivilegeType type;

        private String description;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public Response(Privilege entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.type = entity.getType();
            this.description = entity.getDescription();
            this.createdAt = entity.getCreatedAt();
            this.updatedAt = entity.getUpdatedAt();
        }

    }

}
