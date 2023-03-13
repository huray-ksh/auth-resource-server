package com.hyuuny.authresourceserver.application;

import com.hyuuny.authresourceserver.application.dto.PostDto.Create;
import com.hyuuny.authresourceserver.application.dto.PostDto.Response;
import com.hyuuny.authresourceserver.domain.entity.Post;
import com.hyuuny.authresourceserver.domain.service.PostDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostDomainService postDomainService;

    @Transactional
    public Response create(Create dto) {
        Post newPost = dto.toEntity();
        Post savedPost = postDomainService.save(newPost);
        return toResponse(savedPost);
    }

    private Response toResponse(Post newPost) {
        return new Response(newPost);
    }

    public Response getPost(Long id) {
        Post existingPost = postDomainService.findPost(id);
        return toResponse(existingPost);
    }

}
