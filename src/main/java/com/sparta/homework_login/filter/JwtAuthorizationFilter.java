package com.sparta.homework_login.filter;

import com.sparta.homework_login.common.JwtUtil;
import com.sparta.homework_login.dto.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스입니다.
 * <p>
 * 요청 헤더에 포함된 JWT 토큰을 검증하고, 유효한 토큰이であれば 사용자 인증 정보를 설정합니다.
 * 이후 필터 체인을 통해 다음 필터로 요청을 전달합니다.
 *
 * @since 2025-01-17
 */
@Slf4j(topic = "JwtAuthorizationFilter: JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 필터 실행 메서드입니다.
     * <p>
     * 요청 객체(req), 응답 객체(res), 필터 체인(filterChain)을 파라미터로 받습니다.
     *
     * @param req         HTTP 요청 객체
     * @param res         HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 관련 예외
     * @throws IOException      IO 관련 예외
     * @since 2025-01-17
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal " + req.getRequestURI());
        String tokenValue = req.getHeader("Authorization");
        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);

            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(tokenValue);
            if (claims == null) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            try {
                setAuthentication(claims.get("username", String.class));
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    /**
     * 인증 정보를 SecurityContextHolder에 설정하는 메소드입니다.
     *
     * @param username JWT 토큰에서 추출한 사용자 이름
     * @since 2025-01-17
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * Authentication 객체를 생성하는 메소드입니다.
     *
     * @param username JWT 토큰에서 추출한 사용자 이름
     * @return 생성된 Authentication 객체
     * @since 2025-01-17
     */
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}