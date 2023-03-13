package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.AuthService;
import com.hyuuny.authresourceserver.application.dto.AuthenticationDto;
import com.hyuuny.authresourceserver.application.dto.AuthenticationDto.LoginDto;
import com.hyuuny.authresourceserver.application.dto.TokenDto.DeleteTokenRequest;
import com.hyuuny.authresourceserver.application.dto.TokenDto.RefreshTokenRequest;
import com.hyuuny.authresourceserver.application.dto.UserAdapter;
import com.hyuuny.authresourceserver.common.abstraction.AbstractController;
import com.hyuuny.authresourceserver.common.dto.ResultResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController extends AbstractController {

    private final AuthService authService;

    @PostMapping(value = "/login", name = "로그인(JWT 발급)")
    public ResponseEntity<ResultResponseDto<AuthenticationDto.UserWithToken>> login(
            @RequestBody @Valid LoginDto dto
    ) {
        UserAdapter authenticatedUser = authService.authentication(dto);
        AuthenticationDto.AccessToken accessToken = authService.generateToken(authenticatedUser);
        return ok(
                new AuthenticationDto.UserWithToken(
                        accessToken,
                        new AuthenticationDto.UserInfo(authenticatedUser)
                ));
    }

    @PostMapping(value = "/token/refresh", name = "jwt 토큰 재발급")
    public ResponseEntity<ResultResponseDto<AuthenticationDto.AccessToken>> refreshToken(
            @RequestBody @Valid RefreshTokenRequest dto
    ) {
        AuthenticationDto.AccessToken accessToken = authService.refreshToken(
                dto.getAccessToken(),
                dto.getRefreshToken()
        );
        return ok(accessToken);
    }

    @DeleteMapping(value = "/token", name = "jwt 토큰 삭제")
    public ResponseEntity<?> disableToken(
            @RequestBody @Valid DeleteTokenRequest dto
    ) {
        authService.disableToken(dto.getAccessToken(), dto.getRefreshToken());
        return noContent();
    }

}
