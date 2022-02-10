package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.UserReview;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserReviewDto {
    private Long userReviewId;
    private Long sellerId;
    private String sellerName;
    private Long buyerId;
    private String buyerName;
    private String buyerImage;
    private Long usedBookId;
    private String usedBookTitle;
    private String content;
    private float rating;
    private LocalDateTime reviewDate;
    private String image;
    private int price;

    public UserReviewDto(UserReview userReview){
        this.userReviewId = userReview.getId();
        this.sellerId = userReview.getOrder().getBook().getSeller().getId();
        this.sellerName = userReview.getOrder().getBook().getSeller().getNickName();
        this.usedBookId = userReview.getOrder().getBook().getId();
        this.usedBookTitle = userReview.getOrder().getBook().getTitle();
        this.content = userReview.getContent();
        this.rating = userReview.getRating();
        this.reviewDate = userReview.getReviewDate();
        this.buyerId = userReview.getOrder().getBuyer().getId();
        this.buyerName = userReview.getOrder().getBuyer().getNickName();
        this.buyerImage = userReview.getOrder().getBuyer().getImage();
        this.image = userReview.getOrder().getBook().getThumbnail();
        this.price = userReview.getOrder().getBook().getPrice();
    }

}
