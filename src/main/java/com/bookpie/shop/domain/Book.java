package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.book.BookDto;
import com.bookpie.shop.domain.enums.Category;
import com.sun.source.util.TaskEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;
    private String introduction;
    private String author;
    private String publisher;
    private int price;
    private Long isbn;

    @Column(name = "category_id")
    private int category_id;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
//    private List<BookReview> reviews = new ArrayList<>();

    public Book(String title, String introduce, String author, String publisher, int price, int category_id, Long isbn) {
        this.title = title;
        this.introduction = introduce;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.category_id = category_id;
        this.isbn = isbn;
    }


    public static Book createBook(BookDto dto) {
        return new Book(dto.getTitle(), dto.getIntroduce(), dto.getAuthor(), dto.getPublisher(),
                dto.getPrice(), dto.getCategory_id(), dto.getIsbn());
    }
}
