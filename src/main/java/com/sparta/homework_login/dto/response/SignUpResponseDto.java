package com.sparta.homework_login.dto.response;

import com.sparta.homework_login.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보 응답 DTO 클래스입니다.
 * 회원 가입 성공 시 회원 가입한 정보를 반환합니다.
 *
 * @since 2025-01-17
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {

    @Schema(example = "Hong Gil Dong", description = "회원 이릅")
    private String userName;

    @Schema(example = "동에 번쩍", description = "닉네임")
    private String nickname;

    @Schema(example = "[\"ROLE_USER\"]", description = "권한")
    private List<AuthorDto> authorities;

    /**
     * User 엔티티를 기반으로 SignUpResponseDto 객체를 생성합니다.
     *
     * @param user 회원 정보를 담고 있는 User 엔티티
     * @return 생성된 SignUpResponseDto 객체
     * @since 2025-01-17
     */
    public static SignUpResponseDto create(User user) {
        return SignUpResponseDto.builder()
                .userName(user.getUsername())
                .nickname(user.getNickname())
                .authorities(createAuthor(user))
                .build();
    }

    /**
     * User 엔티티의 권한 정보를 AuthorDto 리스트로 변환합니다.
     *
     * @param user 회원 정보를 담고 있는 User 엔티티
     * @return 권한 정보를 담은 AuthorDto 리스트
     * @since 2025-01-17
     */
    private static List<AuthorDto> createAuthor(User user) {
        List<AuthorDto> authorities = new ArrayList<>();
        authorities.add(AuthorDto.create(user.getUserRole().toString()));
        return authorities;
    }
}
