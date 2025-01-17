package com.sparta.homework_login.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 권한 정보를 담는 응답 DTO 클래스입니다.
 *
 * @since 2025-01-17
 */
@Getter
@Builder
public class AuthorDto {
    private final String authorityName;
}
