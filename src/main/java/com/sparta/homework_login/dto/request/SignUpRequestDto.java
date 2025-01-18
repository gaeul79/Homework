package com.sparta.homework_login.dto.request;

import com.sparta.homework_login.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO 클래스
 *
 * @since 2025-01-17
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @Schema(example = "Hong Gil Dong", description = "사용자 이름")
    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @Schema(example = "1q2w3e4r#", description = "비밀번호")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 [8 ~ 20]글자 이내이며, [영문 + 숫자 + 특수문자]를 최소 1글자씩 포함해야 합니다.")
    private String password;

    @Schema(example = "동에 번쩍", description = "닉네임")
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    public User convertDtoToEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
