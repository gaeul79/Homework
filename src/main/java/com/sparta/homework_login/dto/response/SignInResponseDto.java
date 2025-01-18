package com.sparta.homework_login.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰을 담는 응답 DTO 클래스입니다.
 * 로그인 성공 시 토큰을 담아 반환합니다.
 *
 * @since 2025-01-17
 */
@Getter
@AllArgsConstructor
public class SignInResponseDto {

    @Schema(example = "Bearer ...", description = "발급된 토큰")
    private final String bearerToken;
}
