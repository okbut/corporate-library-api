package com.okbut.api.checkout.domain;

import com.okbut.api.books.domain.Book;
import com.okbut.api.users.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckOutFixtures {

    public static CheckOut firstCheckoutByUser(User user, Book book) {
        return CheckOut.builder()
                .id(1L)
                .checkoutDtm(LocalDateTime.now())
                .dueDtm(LocalDate.now().plusDays(14).atTime(20, 0))
                .isRenewed(0)
                .user(user)
                .book(book)
                .build();
    }

    public static CheckOut secondCheckoutByUser(User user, Book book) {
        return CheckOut.builder()
                .id(1L)
                .checkoutDtm(LocalDateTime.now())
                .dueDtm(LocalDate.now().plusDays(14).atTime(20, 0))
                .isRenewed(0)
                .user(user)
                .book(book)
                .build();
    }
}
