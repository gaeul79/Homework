package com.sparta.homework_login.repository;

import com.sparta.homework_login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 유저 엔티티를 위한 JPA 레포지토리입니다.
 *
 * @since 2025-01-17
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
