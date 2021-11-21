package apiserver.app.users.infrastructure;

import apiserver.app.users.domain.Member;
import apiserver.app.users.domain.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface UserJpaRepository
        extends UserRepository, CrudRepository<Member, Long> {
}
