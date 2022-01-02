package com.bookpie.shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ReviewLike {

    @Id @GeneratedValue
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_review_id")
    BookReview book_review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    public ReviewLike(BookReview bookReview, User user) {
        this.book_review = bookReview;
        this.user = user;
    }

    public static ReviewLike createReviewLike(BookReview bookReview, User user) {
        return new ReviewLike(bookReview, user);
    }

}
