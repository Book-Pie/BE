package com.bookpie.shop.dto;

import lombok.Data;

@Data
public class UserReviewUpdateDto {
    private Long userReviewId;
    private String content;
    private float rating;
}
