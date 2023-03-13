package com.hyuuny.authresourceserver.application.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {

        @NotEmpty
        private String accessToken;

        @NotEmpty
        private String refreshToken;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteTokenRequest {

        @NotEmpty
        private String accessToken;

        @NotEmpty
        private String refreshToken;

    }

}
