package com.sparta.homework_login.dto.security;

import com.sparta.homework_login.entity.User;
import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 UserDetails 인터페이스
 *
 * @since 2025-01-17
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 로그인 시 사용되는 메서드입니다.
     *
     * @param username 로그인에 사용할 사용자 이름
     * @return UserDetails 구현체(UserDetailsImpl)
     * @throws UsernameNotFoundException username에 해당하는 사용자가 없을 때 발생하는 예외
     * @since 2025-01-17
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("loadUserByUsername " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.toString()));
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getUserRole());
    }
}
