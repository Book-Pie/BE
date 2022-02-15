package com.bookpie.shop.repository;

import com.bookpie.shop.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    @Query(value = "select * from review_like r where r.book_review_id = :reviewId", nativeQuery = true)
    List<ReviewLike> findAllByReviewId(@Param("reviewId") Long reviewId);
}
