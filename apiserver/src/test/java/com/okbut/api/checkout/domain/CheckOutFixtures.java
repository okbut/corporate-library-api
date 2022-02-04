package com.okbut.api.checkout.domain;

import com.okbut.api.books.domain.Book;
import com.okbut.api.users.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckOutFixtures {

    /**
     * 반납된 대출 정보
     * @param user 사용자
     * @param book 도서
     * @return 해당 대출 정보
     */
    public static CheckOut returnedCheckout(final User user, final Book book) {
        return CheckOut.builder()
                .id(1L)
                .checkoutDtm(LocalDateTime.now())
                .dueDtm(LocalDate.now().plusDays(14).atTime(20, 0))
                .returnDtm(LocalDate.now().plusDays(12).atTime(20, 0))
                .isRenewed(0)
                .user(user)
                .book(book)
                .build();
    }

    /**
     * 반납되지 않은 대출 정보
     * @param user 사용자
     * @param book 도서
     * @return 해당 대출 정보
     */
    public static CheckOut duringCheckout(final User user, final Book book) {
        return CheckOut.builder()
                .id(1L)
                .checkoutDtm(LocalDateTime.now())
                .dueDtm(LocalDate.now().plusDays(14).atTime(20, 0))
                .returnDtm(null)
                .isRenewed(0)
                .user(user)
                .book(book)
                .build();
    }
}
