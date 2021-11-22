package com.okbut.api.users.application;

import com.okbut.api.users.domain.Member;
import com.okbut.api.users.domain.UserRepository;
import com.okbut.error.expcetion.UserNotFoundException;
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
