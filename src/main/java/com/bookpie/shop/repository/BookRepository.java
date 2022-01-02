package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
