package com.sparta.homework_login.dto.security;

import com.sparta.homework_login.entity.User;
import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.exception.BusinessException;
import com.sparta.homework_login.repository.UserRepository;
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
@Slf4j(topic = "UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인 시 사용되는 메서드입니다.
     *
     * @param nickname 로그인에 사용할 닉네임
     * @return UserDetails 구현체(UserDetailsImpl)
     * @throws UsernameNotFoundException nickname에 해당하는 사용자가 없을 때 발생하는 예외
     * @since 2025-01-17
     */
    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        log.info("loadUserByUsername " + nickname);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getUserRole());
    }
}
