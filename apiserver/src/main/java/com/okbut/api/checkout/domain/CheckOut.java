package com.okbut.api.checkout.domain;

import com.okbut.api.books.domain.Book;
import com.okbut.api.users.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "CHECK_OUT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CheckOut {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "CHECKOUT_DTM", nullable = false)
    private LocalDateTime checkoutDtm;

    @Column(name = "DUE_DTM", nullable = false)
    private LocalDateTime dueDtm;

    @Column(name = "RETURN_DTM")
    private LocalDateTime returnDtm;

    @Column(name = "IS_RENEWED", nullable = false)
    private int isRenewed;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @Builder
    public CheckOut(Long id, LocalDateTime checkoutDtm, LocalDateTime dueDtm, LocalDateTime returnDtm, int isRenewed, User user, Book book) {
        this.id = id;
        this.checkoutDtm = checkoutDtm;
        this.dueDtm = dueDtm;
        this.returnDtm = returnDtm;
        this.isRenewed = isRenewed;
        this.user = user;
        this.book = book;
    }
}
