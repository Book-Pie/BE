package com.bookpie.shop.repository;

import com.bookpie.shop.domain.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    // 내가 쓴 도서리뷰 조회
    Page<BookReview> findAllByUserId(@Param("user_id") Long user_id, @Param("pageable") Pageable pageable);
    // 해당 책에 대한 도서리뷰 조회
    Page<BookReview> findAllByIsbn(@Param("isbn") String isbn, Pageable pageable);
    // 해당 책에 대한 나의 도서리뷰 조회
    @Query(value = "select * from book_review b where b.isbn = :isbn and b.user_id = :user_id", nativeQuery = true)
    BookReview findMyReview(@Param("isbn") String isbn, @Param("user_id") Long user_id);
}
