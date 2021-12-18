package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ReviewLike {

    @Id @GeneratedValue
    @Column(name = "revie_like_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    BookReview review;

    @OneToOne(fetch = FetchType.LAZY)
    User user;

}
