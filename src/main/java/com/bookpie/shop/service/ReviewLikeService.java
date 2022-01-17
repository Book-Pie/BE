package com.bookpie.shop.service;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.ReviewLike;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.domain.dto.book_review.ReviewLikeDto;
import com.bookpie.shop.repository.BookReviewRepository;
import com.bookpie.shop.repository.ReviewLikeRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReviewLikeService {
    @Autowired
    private ReviewLikeRepository reviewLikeRepository;
    @Autowired
    private BookReviewRepository bookReviewRepository;
    @Autowired
    private UserRepository userRepository;

    public ReviewLikeDto like(BookReviewDto dto) {
        // 해당 리뷰가 있는지 확인
        BookReview bookReview = bookReviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        // 해당 회원이 좋아요를 눌렀는지 안눌렀는지 확인
        List<ReviewLike> reviewLikes = reviewLikeRepository.findAllByReviewId(dto.getReviewId());
        for (ReviewLike reviewLike : reviewLikes) {
            if (reviewLike.getUser().getId() == user.getId()) {
                reviewLikeRepository.delete(reviewLike);
                return ReviewLikeDto.createDto(reviewLike, false);
            }
        }

        // 연관 관계 매핑
        ReviewLike reviewLike = ReviewLike.createReviewLike(bookReview, user);
        bookReview.getReviewLikes().add(reviewLike);

        ReviewLike created = reviewLikeRepository.save(reviewLike);
        return ReviewLikeDto.createDto(created, true);
    }
}
