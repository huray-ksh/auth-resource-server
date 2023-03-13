package com.hyuuny.authresourceserver.domain.repository;

import com.hyuuny.authresourceserver.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAccountUsername(String username);

}
