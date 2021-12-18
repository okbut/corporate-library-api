package com.okbut.api.users.infrastructure;

import com.okbut.api.users.domain.User;
import com.okbut.api.users.domain.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface UserJpaRepository
        extends UserRepository, JpaRepository<User, Long> {
}
