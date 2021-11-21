package apiserver.app.users.controller;

import apiserver.app.users.application.UserService;
import apiserver.app.users.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Member findUser(final @PathVariable Long id) {
        return userService.findUser(id);
    }
}
