package com.sparta.homework_login.mock;

import com.sparta.homework_login.enums.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {

    long id() default 1L;

    String username() default "Hong";

    String password() default "1q2w3e4r#";

    String nickname() default "동에 번쩍";

    UserRole userRole() default UserRole.ROLE_USER;
}
