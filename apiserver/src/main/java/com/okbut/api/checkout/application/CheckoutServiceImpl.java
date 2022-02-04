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
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final UserService userService;

    public List<CheckOut> listCheckoutHistory(final Long userId) {
        final User user = userService.findUser(userId);
        return checkoutRepository.findByUserAndReturnDtmNotNull(user);
    }

    public List<CheckOut> listCheckout(final Long userId) {
        final User user = userService.findUser(userId);
        return checkoutRepository.findByUserAndReturnDtmNull(user);
    }
}
