package com.sparta.homework_login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO 클래스
 *
 * @since 2025-01-17
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {

    @Schema(example = "Hong Gil Dong", description = "사용자 이름")
    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @Schema(example = "1q2w3e4r#", description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
