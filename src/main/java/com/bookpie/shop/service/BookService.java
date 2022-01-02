package com.bookpie.shop.service;

import com.bookpie.shop.domain.Book;
import com.bookpie.shop.domain.BookCategory;
import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.domain.dto.book.BookDto;
import com.bookpie.shop.repository.BookCategoryRepository;
import com.bookpie.shop.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    // 카테고리 추가
    public String addCategory(BookCategoryDto dto) {

        if (dto.getParent_id() == null) {  // 부모 카테고리 추가
            BookCategory category = BookCategory.createParentCategory(dto);
            bookCategoryRepository.save(category);
        } else {  // 자식 카테고리 추가
            // 부모 카테고리
            BookCategory parentCategory = bookCategoryRepository.findById(dto.getParent_id())
                    .orElseThrow(() -> new IllegalArgumentException("해당 부모 카테고리는 존재하지 않습니다."));

            // 자식 카테고리
            BookCategory subCategory = BookCategory.createSubCategory(dto, parentCategory);
            bookCategoryRepository.save(subCategory);

            // 연관 관계 매핑 (자식태그 -> 부모태그)
            List<BookCategory> subList = parentCategory.getSubCategory();
            subList.add(subCategory);
        }

        return dto.getCategoryName() + " 카테고리 추가";
    }

    // 카테고리 삭제
    public String deleteCategory(Long category_id) {
        BookCategory bookCategory = bookCategoryRepository.findById(category_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리는 존재하지 않습니다."));

        bookCategoryRepository.delete(bookCategory);
        return bookCategory.getCategoryName() + " 카테고리가 삭제되었습니다.";
    }

    // 카테고리 조회
    public List<BookCategoryDto> getAll() {
        return bookCategoryRepository.findAllCategory()
                .stream().map(category -> BookCategoryDto.createDto(category))
                .collect(Collectors.toList());
    }

    // 도서 추가
    public BookDto create(BookDto dto) {
        Book book = Book.createBook(dto);
        Book book1 = bookRepository.save(book);
        return BookDto.createDto(book1);
    }

    public String delete(Long book_id) {
        Book book = bookRepository.findById(book_id).
                orElseThrow(() -> new IllegalArgumentException("해당 도서는 존재하지 않습니다."));
        bookRepository.delete(book);
        return book.getTitle() + " 도서가 삭제되었습니다.";
    }
}

