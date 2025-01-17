package com.sparta.homework_login.enums;

import com.sparta.homework_login.exception.BusinessException;

import java.util.Arrays;

/**
 * 사용자 권한 종류를 나타내는 열거형입니다.
 *
 * @since 2025-01-17
 */
public enum UserRole {
    ROLE_ADMIN,
    ROLE_USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_ROLE_NOT_FOUND));
    }
}
