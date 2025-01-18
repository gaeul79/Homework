package com.sparta.homework_login.dummy;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<SecurityMockUser> {

    @Override
    public SecurityContext createSecurityContext(SecurityMockUser user) {
        String username = user.username();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                "",
                List.of(new SimpleGrantedAuthority(user.userRole().toString())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        return context;
    }
}
