package com.okbut.api.users.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<Member> findById(Long id);
}
