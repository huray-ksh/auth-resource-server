package com.hyuuny.authresourceserver.application;

import com.hyuuny.authresourceserver.application.dto.AuthenticationDto;
import com.hyuuny.authresourceserver.application.dto.UserAdapter;
import com.hyuuny.authresourceserver.code.ResponseCode;
import com.hyuuny.authresourceserver.common.exception.HttpStatusMessageException;
import com.hyuuny.authresourceserver.common.security.provider.JwtProvider;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.Role.RoleType;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.repository.RoleRepository;
import com.hyuuny.authresourceserver.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserDomainService userDomainService;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public UserAdapter authentication(AuthenticationDto.LoginDto dto) {
        User existingUser = userDomainService.getUser(dto.getUsername());
        Role userRole = userDomainService.findRole(existingUser.getRole().getType());
        List<Privilege.PrivilegeType> privilegeTypes = existingUser.getPrivilegeTypes();
        existingUser.signIn(dto.getPassword(), passwordEncoder);
        return new UserAdapter(existingUser, privilegeTypes);
    }

    public AuthenticationDto.AccessToken generateToken(UserAdapter user) {
        return jwtProvider.issueLoginToken(user.getUserId()).getAccessToken();
    }

    public AuthenticationDto.AccessToken refreshToken(String accessToken, String refreshToken) {
        jwtProvider.verifyRefreshToken(refreshToken);
        AuthenticationDto.UserWithToken userWithToken = jwtProvider.reissueLoginToken(refreshToken);
        this.disableToken(accessToken, refreshToken);
        return userWithToken.getAccessToken();
    }

    public void disableToken(String accessToken, String refreshToken) {
        jwtProvider.disableToken(accessToken);
        jwtProvider.disableToken(refreshToken);
        jwtProvider.deleteRefreshToken(refreshToken);
    }

}
