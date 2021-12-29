package com.bookpie.shop.controller;

import com.bookpie.shop.service.BookService;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bookpie.shop.utils.ApiUtil.success;
@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    // 도서 상세 검색 (NAVER api)
    @GetMapping("/api/book")
    public ApiResult getOne(@Param("keyword") String keyword) {
        return success(bookService.searchPlace(keyword));
    }
}
