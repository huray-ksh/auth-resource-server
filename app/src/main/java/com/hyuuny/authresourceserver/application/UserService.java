package com.hyuuny.authresourceserver.application;

import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.application.dto.UserDto.Response;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDomainService userDomainService;

    @Transactional
    public Response signUp(UserDto.SignUpRequest request) {
        User signUpUser = userDomainService.signUp(request);
        return new Response(signUpUser);
    }

    public Response getUser(Long id) {
        User existingUser = userDomainService.getUser(id);
        return new Response(existingUser);
    }

}
