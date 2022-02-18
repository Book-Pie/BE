package com.bookpie.shop.domain.dto.book_review;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.ReviewLike;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class BookReviewDto {
    private Long reviewId;
    private String isbn;
    private Long userId;
    private String content;
    private float rating;

    private String nickName;
    private int reviewLikeCount;
    private LocalDateTime reviewDate;
    private String category;
    @Builder.Default
    private Boolean likeCheck = false;
    private String profile;

    public static BookReviewDto createDto(BookReview bookReview, Long user_id) {
        return BookReviewDto.builder()
                .reviewId(bookReview.getId())
                .userId(bookReview.getUser().getId())
                .nickName(bookReview.getUser().getNickName())
                .content(bookReview.getContent())
                .rating(bookReview.getRating())
                .isbn(bookReview.getIsbn())
                .reviewLikeCount(bookReview.getLikeCnt())
                .reviewDate(bookReview.getReviewDate())
                .category(bookReview.getCategory())
                .profile(bookReview.getUser().getImage())
                .likeCheck(likeCheck(bookReview, user_id))
                .build();
    }

    // 해당 회원이 좋아요를 눌렀는지 안눌렀는지 확인
    private static Boolean likeCheck(BookReview bookReview, Long user_id) {
        if (user_id == 0L) return false;

        List<ReviewLike> reviewLikeList = bookReview.getReviewLikes();
        for(ReviewLike reviewLike : reviewLikeList) {
            if (reviewLike.getUser().getId().equals(user_id)) return true;
        }
        return false;
    }

}
