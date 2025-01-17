package com.sparta.homework_login.config;

import com.sparta.homework_login.common.JwtUtil;
import com.sparta.homework_login.dto.security.UserDetailsServiceImpl;
import com.sparta.homework_login.filter.JwtAuthenticationFilter;
import com.sparta.homework_login.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스입니다.
 * <p>
 * JWT 기반의 인증 및 인가를 처리하기 위한 보안 설정을 정의합니다.
 *
 * @since 2025-01-17
 */
@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * AuthenticationManager를 Bean으로 등록합니다.
     * <p>
     * Spring Security의 인증을 관리하는 객체로, AuthenticationProvider를 통해 인증을 처리합니다.
     *
     * @param configuration Authentication 설정 객체
     * @return AuthenticationManager 인증 매니저
     * @throws Exception 예외 발생 시 처리
     * @since 2025-01-17
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * JWT 인증 필터를 생성하여 Bean으로 등록합니다.
     * <p>
     * 사용자가 로그인할 때 인증을 수행하며, 성공 시 JWT 토큰을 생성합니다.
     *
     * @return JwtAuthenticationFilter JWT 인증 필터
     * @throws Exception 예외 발생 시 처리
     * @since 2025-01-17
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /**
     * JWT 인가 필터를 생성하여 Bean으로 등록합니다.
     * <p>
     *
     * @return JwtAuthorizationFilter JWT 인가 필터
     * @since 2025-01-17
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    /**
     * Spring Security의 보안 필터 체인을 설정합니다.
     *
     * @param http Spring Security HTTP 보안 설정 객체
     * @return SecurityFilterChain 보안 필터 체인
     * @throws Exception 예외 발생 시 처리
     * @since 2025-01-17
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/auth/'로 시작하는 요청 모두 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 접근 불가 페이지
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedPage("/forbidden.html"));

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DaoAuthenticationProvider를 설정하여 Bean으로 등록합니다.
     *
     * @return DaoAuthenticationProvider 인증 제공자
     * @since 2025-01-17
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false); // UsernameNotFoundException을 유지하도록 설정
        return provider;
    }
}
