package com.bookpie.shop.domain.dto.book_review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewLikeDto {
    private Long id;
    private Long review_id;
    private Long user_id;
}
