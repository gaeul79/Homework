package com.sparta.homework_login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 확인 요청 DTO 클래스
 *
 * @since 2025-02-13
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordCheckRequestDto {
    @Schema(example = "1q2w3e4r#", description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
