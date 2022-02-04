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

    @GetMapping("/users/{userId}/checkout/history")
    public List<CheckOut> listCheckoutHistory(final @PathVariable Long userId) {
        return checkoutService.listCheckoutHistory(userId);
    }

    @GetMapping("/users/{userId}/checkout")
    public List<CheckOut> listCheckout(final @PathVariable Long userId) {
        return checkoutService.listCheckout(userId);
    }
}
