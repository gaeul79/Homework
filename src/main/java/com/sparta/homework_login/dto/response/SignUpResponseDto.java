package com.sparta.homework_login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 회원 정보 응답 DTO 클래스입니다.
 * 회원 가입 성공 시 회원 가입한 정보를 반환합니다.
 *
 * @since 2025-01-17
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {

    private String userName;
    private String nickname;
    private List<AuthorDto> authorities;
}
