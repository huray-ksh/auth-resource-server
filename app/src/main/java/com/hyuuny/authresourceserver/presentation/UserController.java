package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.UserService;
import com.hyuuny.authresourceserver.application.dto.UserDto;
import com.hyuuny.authresourceserver.application.dto.UserDto.Response;
import com.hyuuny.authresourceserver.common.abstraction.AbstractController;
import com.hyuuny.authresourceserver.common.dto.ResultResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController extends AbstractController {

    private final UserService userService;

    @PostMapping(name = "회원가입")
    public ResponseEntity<ResultResponseDto<Response>> signUp(
            @RequestBody UserDto.SignUpRequest signUpRequest
    ) {
        Response signUpUser = userService.signUp(signUpRequest);
        return created(signUpUser);
    }

    @PreAuthorize("isAuthenticated() and @authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R')")
    @GetMapping(value = "/{id}", name = "회원 상세정보")
    public ResponseEntity<ResultResponseDto<Response>> getUser(@PathVariable Long id) {
        Response existingUser = userService.getUser(id);
        return ok(existingUser);
    }

}
