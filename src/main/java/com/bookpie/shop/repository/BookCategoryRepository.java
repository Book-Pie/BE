package com.bookpie.shop.repository;

import com.bookpie.shop.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository <BookCategory, Long> {
//    @Query(value = "select c.book_category_id, c.category_name, coalesce(c.parent_category_id, 0) as parent_category_id" +
//            " from book_category c order by c. parent_category_id", nativeQuery = true)
    @Query(value = "select c.book_category_id, c.category_name, coalesce(c.parent_category_id, 0) as parent_category_id" +
        " from book_category c where coalesce(c.parent_category_id, 0) = 0", nativeQuery = true)
    List<BookCategory> findAllCategory();
}
