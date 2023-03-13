package com.hyuuny.authresourceserver.domain.service;

import com.hyuuny.authresourceserver.common.exception.HttpStatusMessageException;
import com.hyuuny.authresourceserver.domain.entity.Post;
import com.hyuuny.authresourceserver.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostDomainService {

    private final PostRepository postRepository;

    public Post save(Post entity) {
        return postRepository.save(entity);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new HttpStatusMessageException(HttpStatus.BAD_REQUEST, id + "번 게시글을 찾을 수 없습니다.")
        );
    }

    public Long delete(Long id) {
        postRepository.deleteById(id);
        return id;
    }

}
