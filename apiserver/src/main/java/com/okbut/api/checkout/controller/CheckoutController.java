package com.okbut.api.checkout.controller;

import com.okbut.api.checkout.application.CheckoutService;
import com.okbut.api.checkout.domain.CheckOut;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/users/{userId}/checkout")
    public List<CheckOut> findCheckoutListOfUser(final @PathVariable Long userId) {
        return checkoutService.findCheckoutListOfUser(userId);
    }
}
