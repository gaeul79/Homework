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

    /**
     * 권한 이름을 기반으로 AuthorDto 객체를 생성합니다.
     *
     * @param authorityName 권한 이름
     * @return 생성된 AuthorDto 객체
     * @since 2025-01-17
     */
    public static AuthorDto create(String authorityName) {
        return AuthorDto.builder()
                .authorityName(authorityName)
                .build();
    }
}
