package com.okbut.api.checkout.infrastructure;

import com.okbut.api.checkout.domain.CheckOut;
import com.okbut.api.checkout.domain.CheckoutRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface CheckoutJpaRepository
        extends CheckoutRepository, CrudRepository<CheckOut, Long> {
}
