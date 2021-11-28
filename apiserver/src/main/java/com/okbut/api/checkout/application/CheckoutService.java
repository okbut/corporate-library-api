package com.okbut.api.checkout.application;

import com.okbut.api.checkout.domain.CheckOut;
import com.okbut.api.checkout.domain.CheckoutRepository;
import com.okbut.api.users.application.UserService;
import com.okbut.api.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final UserService userService;

    public List<CheckOut> findCheckoutListOfUser(Long userId) {

        User user = userService.findUser(userId);

        return checkoutRepository.findByUser(user);
    }
}
