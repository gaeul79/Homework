package com.sparta.homework_login.common;

import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.enums.UserRole;
import com.sparta.homework_login.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Jwt (JSON Web Token) 유틸리티 클래스입니다.
 *
 * @since 2025-01-17
 */
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Jwt 토큰을 생성합니다.
     *
     * @param userId   사용자 아이디
     * @param userRole 사용자 권한
     * @param username 사용자 이름
     * @param nickname 사용자 닉네임
     * @return 생성된 JWT 토큰 (String)
     * @since 2025-01-17
     */
    public String createToken(Long userId, UserRole userRole, String username, String nickname) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("userRole", userRole)
                        .claim("username", username)
                        .claim("nickname", nickname)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    /**
     * 쿠키에 저장된 토큰 값에서 토큰 자체만 추출합니다.
     * (토큰 앞에 붙은 "Bearer " 문자열 제거)
     *
     * @param tokenValue 쿠키에서 읽어온 토큰 값 (String)
     * @return 토큰 자체 값 (String)
     * @throws BusinessException 토큰 값이 유효하지 않거나 없는 경우 발생
     * @since 2025-01-17
     */
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
    }

    /**
     * 유효한 토큰에서 사용자 정보를 추출합니다.
     *
     * @param token 유효한 JWT 토큰 (String)
     * @return 토큰에 포함된 사용자 정보 (Claims 객체)
     * @since 2025-01-17
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
