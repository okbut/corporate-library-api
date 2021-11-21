package apiserver.app.users.application;

import apiserver.app.users.domain.Member;

public interface UserService {
    Member findUser(Long userId);
}
