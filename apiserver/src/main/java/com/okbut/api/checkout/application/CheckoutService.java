package com.okbut.api.checkout.application;

import com.okbut.api.checkout.domain.CheckOut;

import java.util.List;

public interface CheckoutService {

    List<CheckOut> listCheckoutHistory(final Long userId);

    List<CheckOut> listCheckout(final Long userId);
}
