package com.sparta.homework_login.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {

  @NotBlank(message = "이름을 입력해주세요")
  private String username;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;
}
