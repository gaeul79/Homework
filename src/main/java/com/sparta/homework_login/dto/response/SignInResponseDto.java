package com.sparta.homework_login.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 토큰을 담는 응답 DTO 클래스입니다.
 * 로그인 성공 시 토큰을 담아 반환합니다.
 *
 * @since 2025-01-17
 */
@Getter
@Builder
public class SignInResponseDto {

    private final String bearerToken;
}
