package com.sparta.homework_login.dto.security;

import com.sparta.homework_login.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 사용자 UserDetails 구현 클래스입니다.
 * Spring Security UserDetails 인터페이스를 구현하여 사용자 권한 정보를 제공합니다.
 *
 * @since 2025-01-17
 */
@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private UserRole userRole;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 사용자 권한 설정 메서드
     *
     * @return 사용자 권한 정보를 담은 컬렉션
     * @since 2025-01-17
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = userRole;
        String authority = role.toString();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
