package com.okbut.api.checkout.domain;

import com.okbut.api.users.domain.User;

import java.util.List;

public interface CheckoutRepository {
    List<CheckOut> findByUserAndReturnDtmNotNull(final User user);

    List<CheckOut> findByUserAndReturnDtmNull(final User user);
}
