package com.okbut.api.books.infrastructure;

import com.okbut.api.books.domain.Book;
import com.okbut.api.books.domain.BookRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface BookJpaRepository
        extends BookRepository, JpaRepository<Book, Long> {
}
