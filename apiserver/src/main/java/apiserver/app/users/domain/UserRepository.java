package apiserver.app.users.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<Member> findById(Long id);
}
