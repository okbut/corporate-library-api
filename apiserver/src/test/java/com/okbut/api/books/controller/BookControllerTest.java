package com.okbut.api.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okbut.api.books.application.BookService;
import com.okbut.api.books.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DisplayName("BookController 클래스")
@WebMvcTest(BookController.class)
@AutoConfigureRestDocs
class BookControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /books 요청은")
    class Describe_post_books {
        final Long isbn = 1234567890123L;
        final Book book =
                Book.builder()
                    .id(1L)
                    .build();

        @BeforeEach
        void mocking() {
            given(bookService.registerBook(any(Book.class))).willReturn(book);
        }

        // TODO : 만약 관리자 권한을 가진 아이디가 주어진다면
        @Test
        @DisplayName("도서를 등록하고 응답한다.")
        void it_register_a_book_and_returns() throws Exception {
            var request = post("/books")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(book));
            mockMvc.perform(request)
                   .andExpect(status().isCreated())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.isbn").value(isbn))
//                   .andExpect(jsonPath("$.[?(@.isbn == '%d')]", isbn).exists())
                   .andDo(print());

            verify(bookService).registerBook(any(Book.class));
        }

        // TODO : 만약 관리자 권한이 없는 아이디가 주어진다면
    }
}
