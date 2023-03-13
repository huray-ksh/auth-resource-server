package com.hyuuny.authresourceserver.presentation;

import com.hyuuny.authresourceserver.application.PostService;
import com.hyuuny.authresourceserver.application.dto.PostDto.Create;
import com.hyuuny.authresourceserver.application.dto.PostDto.Response;
import com.hyuuny.authresourceserver.common.abstraction.AbstractController;
import com.hyuuny.authresourceserver.common.dto.ResultResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/posts")
@RestController
public class PostAdminController extends AbstractController {

    private final PostService postService;

    @PreAuthorize("@authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_ORGANIZATION', 'OP')")
    @PostMapping(name = "게시물 등록(관리자만 등록 가능)")
    public ResponseEntity<ResultResponseDto<Response>> createPost(@RequestBody @Valid Create dto) {
        Response savedPost = postService.createPost(dto);
        return created(savedPost);
    }

    @PreAuthorize("@authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_USER', 'R')")
    @GetMapping(value = "/{id}", name = "게시물 상세 조회")
    public ResponseEntity<ResultResponseDto<Response>> getPost(@PathVariable Long id) {
        Response existingPost = postService.getPost(id);
        return ok(existingPost);
    }

    @PreAuthorize("@authResourcePermissionEvaluator.hasPrivilege('PRIVILEGE_ORGANIZATION', 'D')")
    @DeleteMapping(value = "/{id}", name = "게시물 삭제(관리자만 삭제 가능)")
    public ResponseEntity<ResultResponseDto<Long>> deletePost(@PathVariable Long id) {
        Long deletePostId = postService.deletePost(id);
        return ok(deletePostId);
    }

}
