package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class BookReview {

    @Id @GeneratedValue
    @Column(name = "book_review_id")
    private Long id;

    private String content;
    private float rating;
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
}
