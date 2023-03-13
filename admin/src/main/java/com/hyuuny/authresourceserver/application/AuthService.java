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
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtProvider jwtProvider;

    public UserAdapter authentication(AuthenticationDto.LoginDto dto) {
        User existingUser = findUser(dto.getUsername());
        Role existingRole = findRole(existingUser.getRole().getType());
        List<Privilege.PrivilegeType> privilegeTypes = existingUser.getPrivilegeTypes();
        return new UserAdapter(existingUser, privilegeTypes);
    }

    private User findUser(String username) {
        return userRepository.findByUserAccountUsername(username).orElseThrow(
                () -> new HttpStatusMessageException(ResponseCode.NO_MATCHED_USERNAME, "일치하는 username이 없습니다.")
        );
    }

    private Role findRole(RoleType roleType) {
        return roleRepository.findByType(roleType).orElseThrow(
                () -> new HttpStatusMessageException(ResponseCode.NO_VALID_AUTHORIZATION, "접근할 수 있는 권한이 없습니다.")
        );
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
