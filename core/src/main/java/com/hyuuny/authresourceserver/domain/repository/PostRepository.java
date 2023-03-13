package com.hyuuny.authresourceserver.domain.repository;

import com.hyuuny.authresourceserver.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
