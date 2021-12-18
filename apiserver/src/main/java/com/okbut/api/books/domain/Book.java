package com.okbut.api.books.domain;

import com.okbut.api.config.audit.AuditEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Book extends AuditEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String contents;

    private String isbn;
    private String classification;
    private String title;
    private String description;
    private String author;
    private Integer publishYear;

    private String shelfLocation;
    private LocalDateTime purchasedAt;
    private Boolean disposed;

    @Builder
    public Book(Long id, String contents, String isbn, String classification, String title, String description, String author, Integer publishYear, String shelfLocation, LocalDateTime purchasedAt, Boolean disposed) {
        this.id = id;
        this.contents = contents;
        this.isbn = isbn;
        this.classification = classification;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publishYear = publishYear;
        this.shelfLocation = shelfLocation;
        this.purchasedAt = purchasedAt;
        this.disposed = disposed;
    }
}
