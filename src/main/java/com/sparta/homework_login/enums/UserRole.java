package com.sparta.homework_login.enums;

import com.sparta.homework_login.exception.BusinessException;
import java.util.Arrays;

public enum UserRole {
  ROLE_ADMIN,
  ROLE_USER;

  public static UserRole of(String role) {
    return Arrays.stream(UserRole.values())
        .filter(r -> r.name().equalsIgnoreCase(role))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_ROLE_NOT_FOUND));
  }
}
