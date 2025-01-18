package com.sparta.homework_login.dummy;

import com.sparta.homework_login.enums.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface SecurityMockUser {
    String username() default "Hong Gil Dong";
    String password() default "1q2w3e4r#";
    String nickname() default "동에 번쩍";
    UserRole userRole() default UserRole.ROLE_USER;
}