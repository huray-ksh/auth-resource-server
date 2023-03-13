package com.hyuuny.authresourceserver.domain.service;

import com.hyuuny.authresourceserver.application.dto.UserAdapter;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.code.ResponseCode;
import com.hyuuny.authresourceserver.common.exception.HttpStatusMessageException;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.Role;
import com.hyuuny.authresourceserver.domain.entity.Role.RoleType;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.repository.PrivilegeRepository;
import com.hyuuny.authresourceserver.domain.repository.RoleRepository;
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserDomainService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    public User signUp(UserDto.SignUpRequest dto) {
        Role role = findRole(dto.getRoleType());
        List<Privilege> privileges = privilegeRepository.findAllByTypeIn(dto.getPrivilegeTypes());
        User newUser = dto.toEntity();
        newUser.signUp(role, privileges);
        return userRepository.save(newUser);
    }

    private Role findRole(RoleType roleType) {
        return roleRepository.findByType(roleType).orElseThrow(
                () -> new HttpStatusMessageException(ResponseCode.BAD_REQUEST, "role을 찾을 수 없습니다.")
        );
    }

    public User getUser(Long id) {
        User existingUser = findUser(id);
        return existingUser;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new HttpStatusMessageException(ResponseCode.BAD_REQUEST, id + "번 회원을 찾을 수 없습니다.")
        );
    }

    public UserAdapter getUserDetails(Long id) {
        User existingUser = findUser(id);
        List<Privilege.PrivilegeType> privilegeTypes = existingUser.getPrivilegeTypes();
        return new UserAdapter(existingUser, privilegeTypes);
    }

}
