package com.okbut.api.checkout.domain;

import com.okbut.api.users.domain.User;

import java.util.List;

public interface CheckoutRepository {
    List<CheckOut> findByUser(User user);
}
