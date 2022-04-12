package com.bookpie.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewCreateDto {

    private Long orderId;
    private String content;
    private float rating;

}
