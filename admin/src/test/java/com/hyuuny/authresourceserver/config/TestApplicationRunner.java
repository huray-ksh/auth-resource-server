package com.hyuuny.authresourceserver.config;


import com.hyuuny.authresourceserver.application.UserService;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.repository.PrivilegeRepository;
import com.hyuuny.authresourceserver.domain.repository.RoleRepository;
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.hyuuny.authresourceserver.Fixture.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
public class TestApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserService userService;


    @Override
    public void run(ApplicationArguments args) {
        if (isEmpty(roleRepository.findAll())) {
            roleRepository.saveAll(aRoles());
        }

        if (isEmpty(privilegeRepository.findAll())) {
            privilegeRepository.saveAll(anAdminPrivileges());
        }

        Optional<User> optionalAdmin = userRepository.findByUserAccountUsername(ADMIN_EMAIL);
        if (optionalAdmin.isEmpty()) {
            UserDto.SignUpRequest adminRequest = anAdmin().build();
            userService.signUp(adminRequest);
        }

        Optional<User> optionalManager = userRepository.findByUserAccountUsername(MANAGER_EMAIL);
        if (optionalManager.isEmpty()) {
            UserDto.SignUpRequest managerRequest = anManager().build();
            userService.signUp(managerRequest);
        }

        Optional<User> optionalUser = userRepository.findByUserAccountUsername(USER_EMAIL);
        if (optionalUser.isEmpty()) {
            UserDto.SignUpRequest userRequest = anUser().build();
            userService.signUp(userRequest);
        }

    }

}
