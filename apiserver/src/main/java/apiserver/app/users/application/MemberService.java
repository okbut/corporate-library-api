package apiserver.app.users.application;

import apiserver.app.config.error.expcetion.UserNotFoundException;
import apiserver.app.users.domain.Member;
import apiserver.app.users.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserService {

    private final UserRepository userRepository;

    public MemberService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Member findUser(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
