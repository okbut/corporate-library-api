package com.okbut.api.users.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
}
