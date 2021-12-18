package com.okbut.api.users.application;

import com.okbut.api.users.domain.User;

public interface UserService {
    User findUser(Long userId);
}
