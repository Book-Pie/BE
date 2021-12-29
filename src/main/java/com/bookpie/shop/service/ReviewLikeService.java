package com.bookpie.shop.service;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.ReviewLike;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.repository.BookReviewRepository;
import com.bookpie.shop.repository.ReviewLikeRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewLikeService {
    @Autowired
    private ReviewLikeRepository reviewLikeRepository;
    @Autowired
    private BookReviewRepository bookReviewRepository;
    @Autowired
    private UserRepository userRepository;

    public String like(Long review_id) {
        // 해당 리뷰가 있는지 확인
        BookReview bookReview = bookReviewRepository.findById(review_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));
        // 해당 유저 확인 (실제로 user_id는 세션값에서 가져옴)
        Optional<User> user = userRepository.findById(1L);
        User objUser = user.orElse(null);

        // 해당 회원이 좋아요를 눌렀는지 안눌렀는지 확인
        List<ReviewLike> reviewLikeList = bookReview.getReviewLikes();
        for (ReviewLike reviewLike : reviewLikeList) {
            if (reviewLike.getUser().getId() == objUser.getId()) {
                reviewLikeRepository.delete(reviewLike);
                return "좋아요 삭제";
            }
        }

        // 연관 관계 매핑
        ReviewLike reviewLike = ReviewLike.createReviewLike(bookReview, objUser);
        bookReview.getReviewLikes().add(reviewLike);

        ReviewLike created = reviewLikeRepository.save(reviewLike);

        return "좋아요 추가";
    }
}
