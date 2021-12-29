package com.bookpie.shop.domain.dto.book_review;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.ReviewLike;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookReviewDto {
    private Long review_id;
    private Long book_id;
    private Long user_id;
    private String content;
    private float rating;

    private String nickname;
    private int reviewLikeCount;
    private LocalDateTime reviewDate;
    private Boolean likeCheck;

    public BookReviewDto(Long review_id, Long book_id, Long user_id, String nickName, String content,
                         float rating, int reviewLikeCount, LocalDateTime reviewDate, Boolean likeCheck) {
        this.review_id = review_id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.nickname = nickName;
        this.content = content;
        this.rating = rating;
        this.reviewLikeCount = reviewLikeCount;
        this.reviewDate = reviewDate;
        this.likeCheck = likeCheck;
    }

//    public static BookReviewDto createBookReviewDto(BookReview bookReview, int reviewLikeCount, String likeCheck) {
//        return new BookReviewDto(bookReview.getId(), bookReview.getBook().getId(), bookReview.getUser().getId(),
//                bookReview.getUser().getNickName(), bookReview.getContent(), bookReview.getRating(), reviewLikeCount,
//                bookReview.getReviewDate(), likeCheck);
//    }
    public static BookReviewDto createBookReviewDto(BookReview bookReview, Long user_id) {
        return new BookReviewDto(bookReview.getId(), bookReview.getBook().getId(), bookReview.getUser().getId(),
                bookReview.getUser().getNickName(), bookReview.getContent(), bookReview.getRating(), bookReview.getReviewLikes().size(),
                bookReview.getReviewDate(), likeCheck(bookReview, user_id));
    }

    private static Boolean likeCheck(BookReview bookReview, Long user_id) {
        List<ReviewLike> reviewLikeList = bookReview.getReviewLikes();
        for(ReviewLike reviewLike : reviewLikeList) {
            if (reviewLike.getUser().getId() == user_id) return true;
        }
        return false;
    }

}
