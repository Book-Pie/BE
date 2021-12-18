package com.bookpie.shop.domain;

import com.bookpie.shop.domain.enums.Category;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Book {

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    private String title;
    private String introduction;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;
    private int price;
    private String image;
    private Category fstCategory;
    private Category sndCategory;
    private float rating;
    private int reviewCount;

    @OneToMany(mappedBy = "book")
    private List<BookReview> reviews = new ArrayList<>();

}
