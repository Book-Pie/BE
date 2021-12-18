package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UsedBookLike {

    @Id @GeneratedValue
    @Column(name="book_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsedBook book;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
