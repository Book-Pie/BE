package com.bookpie.shop.repository;

import com.bookpie.shop.domain.BookReview;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    // 내가 쓴 도서리뷰 조회
    Page<BookReview> findAllByUserId(@Param("user_id") Long user_id, @Param("pageable") Pageable pageable);

    // 해당 책에 대한 도서리뷰 조회
    Page<BookReview> findAllByIsbn(@Param("isbn") String isbn, Pageable pageable);

    // 해당 책에 대한 나의 도서리뷰 조회
    @Query(value = "select * from book_review b where b.isbn = :isbn and b.user_id = :user_id", nativeQuery = true)
    BookReview findMyReview(@Param("isbn") String isbn, @Param("user_id") Long user_id);

    // 도서 리뷰 좋아요 카운트 증가
    @Modifying
    @Transactional
    @Query(value = "update book_review set like_cnt = like_cnt+1 where book_review_id = :reviewId", nativeQuery = true)
    void increaseLikeCnt(@Param("reviewId") Long reviewId);

    // 도서 리뷰 좋아요 카운트 감소
    @Modifying
    @Transactional
    @Query(value = "update book_review set like_cnt = like_cnt-1 where book_review_id = :reviewId", nativeQuery = true)
    void decreaseLikeCnt(@Param("reviewId") Long reviewId);

    // 해당 책에 대한 도서 리뷰 중 좋아요가 가장 많은 2개의 리뷰
    @Query(value = "select * from book_review where isbn=:isbn and like_cnt > 0" +
            " order by like_cnt DESC LIMIT 2", nativeQuery = true)
    List<BookReview> bestReview(@Param("isbn") String isbn);

    // 회원이 가장 많은 리뷰를 남긴 카테고리 top 5
    @Query(value = "select category, count(*) as count from book_review" +
            " where user_id = :user_id" +
            " group by category" +
            " order by count(*) DESC LIMIT 5", nativeQuery = true)
    List<JSONObject> myCategory(@Param("user_id") Long user_id);

    // 해당 책의 평균 평점
    @Query(value = "select avg(rating) from book_review " +
            "where isbn = :isbn", nativeQuery = true)
    Double averageRating(@Param("isbn") String isbn);

}
