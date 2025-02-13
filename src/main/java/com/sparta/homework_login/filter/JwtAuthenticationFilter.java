package com.sparta.homework_login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.homework_login.common.JsonUtil;
import com.sparta.homework_login.common.JwtUtil;
import com.sparta.homework_login.dto.request.SignInRequestDto;
import com.sparta.homework_login.dto.response.SignInResponseDto;
import com.sparta.homework_login.dto.security.UserDetailsImpl;
import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.enums.UserRole;
import com.sparta.homework_login.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스입니다.
 * <p>
 * 사용자 로그인 시도를 처리하고, 성공 시에는 JWT 토큰을 생성하여 응답 헤더에 포함합니다.
 * 실패 시에는 오류 코드를 설정하여 클라이언트에게 알려줍니다.
 *
 * @since 2025-01-17
 */
@Slf4j(topic = "JwtAuthenticationFilter: 로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JsonUtil jsonUtil) {
        this.jwtUtil = jwtUtil;
        this.jsonUtil = jsonUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    /**
     * 로그인 시도를 수행하는 메서드입니다.
     * <p>
     * 요청 본문(body)에서 로그인 정보(SignInRequestDto)를 파싱하고,
     * UsernamePasswordAuthenticationToken 객체를 생성하여 스프링 시큐리티 인증 매니저에게 전달합니다.
     *
     * @param req HTTP 요청 객체
     * @param res HTTP 응답 객체
     * @return 인증 객체(Authentication)
     * @throws AuthenticationException 로그인 실패 시 발생하는 예외
     * @since 2025-01-17
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            SignInRequestDto requestDto = objectMapper.readValue(req.getInputStream(), SignInRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.JSON_INVALID);
        }
    }

    /**
     * 로그인 성공 시 수행하는 메서드입니다.
     * <p>
     * 인증된 사용자 정보(UserDetailsImpl)를 기반으로 JWT 토큰을 생성하고,
     * 응답 헤더와 body부분에 토큰을 담아 반환합니다.
     *
     * @param req        HTTP 요청 객체
     * @param res        HTTP 응답 객체
     * @param chain      필터 체인
     * @param authResult 인증 결과(Authentication)
     * @since 2025-01-17
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 JWT 생성");

        Long id = ((UserDetailsImpl) authResult.getPrincipal()).getId();
        UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUserRole();
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getNickname();

        String token = jwtUtil.createToken(id, role, username, nickname);
        String json = objectMapper.writeValueAsString(new SignInResponseDto(token));

        res.setHeader("Authorization", token);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(json);
    }

    /**
     * 로그인 실패 시 수행하는 메소드입니다.
     *
     * @param req HTTP 요청 객체
     * @param res HTTP 응답 객체
     * @param ex  인증 실패에 대한 예외 정보
     * @since 2025-01-17
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) {
        log.info("로그인 실패");
        try {
            ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
            if (ex instanceof UsernameNotFoundException) {
                errorCode = ErrorCode.USER_NOT_FOUND;
            } else if (ex instanceof BadCredentialsException) {
                errorCode = ErrorCode.USER_PASSWORD_NOT_MATCH;
            }
            jsonUtil.writeBody(req, res, errorCode);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.JSON_INVALID);
        }
    }
}