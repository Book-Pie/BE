package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UserReviewCreateDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReview {

    @Id @GeneratedValue
    @Column(name = "user_review_id")
    private Long id;

    private String content;
    private float rating;
    private LocalDateTime reviewDate;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @Builder
    public UserReview(String content,float rating,LocalDateTime reviewDate,Order order){
        this.content = content;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.order = order;
    }
    public static UserReview createUserReview(UserReviewCreateDto dto, Order odr){
        UserReview review = UserReview.builder()
                .content(dto.getContent())
                .rating(dto.getRating())
                .reviewDate(LocalDateTime.now())
                .order(odr).build();
        odr.addReview(review);
        return review;
    }

    public void addOrder(Order order){
        this.order = order;
    }
}
