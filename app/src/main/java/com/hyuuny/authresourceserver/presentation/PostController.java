package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.PostService;
import com.hyuuny.authresourceserver.application.dto.PostDto;
import com.hyuuny.authresourceserver.application.dto.PostDto.Response;
import com.hyuuny.authresourceserver.common.abstraction.AbstractController;
import com.hyuuny.authresourceserver.common.dto.ResultResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController extends AbstractController {

    private final PostService postService;

    @PreAuthorize("isAuthenticated() and @authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R')")
    @PostMapping(name = "게시물 등록")
    public ResponseEntity<ResultResponseDto<Response>> createPost(@RequestBody @Valid PostDto.Create dto) {
        Response savedPost = postService.create(dto);
        return created(savedPost);
    }

    @PreAuthorize("isAuthenticated() and @authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R')")
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<Response>> getPost(@PathVariable Long id) {
        Response existingPost = postService.getPost(id);
        return ok(existingPost);
    }

}
