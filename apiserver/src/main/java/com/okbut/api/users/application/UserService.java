package com.okbut.api.users.application;

import com.okbut.api.users.domain.Member;

public interface UserService {
    Member findUser(Long userId);
}
