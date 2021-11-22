package com.okbut.api.users.infrastructure;

import com.okbut.api.users.domain.Member;
import com.okbut.api.users.domain.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface UserJpaRepository
        extends UserRepository, CrudRepository<Member, Long> {
}
