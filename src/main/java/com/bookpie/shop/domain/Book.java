package com.bookpie.shop.domain;

import com.bookpie.shop.domain.enums.Category;
import com.sun.source.util.TaskEvent;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;
    private String introduction;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;
    private int price;
    private String image;

    @Column(name = "fst_category")
    @Enumerated(EnumType.STRING)
    private Category fstCategory;

    @Column(name = "snd_category")
    @Enumerated(EnumType.STRING)
    private Category sndCategory;

    @Column(name = "category_id")
    private Long category_id;

    private float rating;

    @Column(name = "review_count")
    private int reviewCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<BookReview> reviews = new ArrayList<>();

}
