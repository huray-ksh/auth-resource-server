package com.hyuuny.authresourceserver.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.Role.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class RoleDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_EMPTY)
    public static class Response implements Serializable {

        private Long id;

        private String name;

        private RoleType type;

        private String description;

        private java.util.List<PrivilegeDto.Response> privileges;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public Response(Role entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.type = entity.getType();
            this.description = entity.getDescription();
            this.privileges = entity.getRolePrivileges().stream()
                    .map(rolePrivilege -> new PrivilegeDto.Response(rolePrivilege.getPrivilege()))
                    .collect(Collectors.toList());
        }
    }

}
