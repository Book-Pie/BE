package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class UserReview {

    @Id @GeneratedValue
    @Column(name = "user_review_id")
    private Long id;

    private String content;
    private float rating;
    private LocalDateTime reviewDate;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;


}
