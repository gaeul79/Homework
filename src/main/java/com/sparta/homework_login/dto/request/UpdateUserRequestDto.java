package com.sparta.homework_login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 수정 DTO 클래스
 *
 * @since 2025-02-13
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {

    @Schema(example = "1q2w3e4r#", description = "확인용 비밀번호")
    @NotBlank(message = "확인용 비밀번호를 입력해주세요")
    private String oriPassword;

    @Schema(example = "Admin123!", description = "변경할 비밀번호")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 [8 ~ 20]글자 이내이며, [영문 + 숫자 + 특수문자]를 최소 1글자씩 포함해야 합니다.")
    private String newPassword;

    @Schema(example = "서에 번쩍", description = "수정할 닉네임")
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;
}
