package com.bookpie.shop.domain.dto.book_review;

import com.bookpie.shop.domain.ReviewLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ReviewLikeDto {
    private Long reviewLikeId;
    private Long reviewId;
    private Long userId;
    private Boolean check;

    public static ReviewLikeDto createDto(ReviewLike reviewLike, Boolean check) {
        return new ReviewLikeDto(reviewLike.getId(), reviewLike.getBook_review().getId(),
                reviewLike.getUser().getId(), check);
    }
}
