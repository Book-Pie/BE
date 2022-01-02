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

    Page<BookReview> findAllByUserId(@Param("user_id") Long user_id, @Param("pageable") Pageable pageable);

    Page<BookReview> findAllByIsbn(@Param("isbn") Long isbn, Pageable pageable);
}
