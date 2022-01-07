package com.okbut.api.books.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {
    Book save(Book book);
}
