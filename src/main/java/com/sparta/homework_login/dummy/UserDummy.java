package com.sparta.homework_login.dummy;

import com.sparta.homework_login.entity.User;

public class UserDummy {

    public static User createDummyUser() {
        String username = "Hong Gil Dong";
        String password = "dkwk73133905@";
        String nickname = "동에 번쩍";
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
