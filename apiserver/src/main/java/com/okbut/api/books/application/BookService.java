package com.okbut.api.books.application;

import com.okbut.api.books.domain.Book;
import com.okbut.api.books.domain.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 도서를 등록하고 리턴합니다.
     *
     * @param book 도서
     * @return 등록한 도서
     */
    public Book registerBook(Book book) {
        return bookRepository.save(book);
    }
}
