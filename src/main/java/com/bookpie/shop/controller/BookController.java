package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.domain.dto.book.BookDto;
import com.bookpie.shop.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;

@Slf4j
@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    // 카테고리 추가 (관리자 권한)
    @PostMapping("/api/book/category")
    public ResponseEntity addCategory(@RequestBody BookCategoryDto dto) {
        return new ResponseEntity(success(bookService.addCategory(dto)), HttpStatus.OK);
    }

    // 카테고리 삭제 (관리자 권한)
    @DeleteMapping("/api/book/category/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity(success(bookService.deleteCategory(categoryId)), HttpStatus.OK);
    }

    // 카테고리 조회
    @GetMapping("/api/book/category")
    public ResponseEntity getAll() {
        return new ResponseEntity(success(bookService.getAll()), HttpStatus.OK);
    }

    // 도서 추가 (테스트용)
    @PostMapping("/api/book")
    public ResponseEntity create(@RequestBody BookDto dto) {
        return new ResponseEntity(success(bookService.create(dto)), HttpStatus.OK);
    }

    // 도서 삭제 (테스트용)
    @DeleteMapping("/api/book/{bookId}")
    public ResponseEntity delete(@PathVariable Long bookId) {
        return new ResponseEntity(success(bookService.delete(bookId)), HttpStatus.OK);
    }

    // 베스트 셀러 조회 (알라딘 api 호출)
    @GetMapping("/api/book/bestseller")
    public ResponseEntity best(@RequestParam(value = "page", required = false) String page,
                               @RequestParam(value="size", required = false) String size) {
        return new ResponseEntity(success(bookService.best(page, size)), HttpStatus.OK);
    }

    // 도서 상세 조회 (알라딘 api 호출)
    @GetMapping("/api/book")
    public ResponseEntity bookDetail (@RequestParam(required = false) String isbn13,
                                      @RequestParam(required = false) String isbn) {
        return new ResponseEntity(success(bookService.bookDetail(isbn13, isbn)), HttpStatus.OK);
    }

    // 도서 키워드로 검색 (알라딘 api 호출)
    @GetMapping("/api/book/search")
    public ResponseEntity searchKeyword(@RequestParam String QueryType, @RequestParam String keyword,
                                        @RequestParam(required = false) String page, @RequestParam(required = false) String size) {
        return new ResponseEntity(success(bookService.searchKeyword(QueryType, keyword, page, size)), HttpStatus.OK);
    }

    // 도서 카테고리별 조회 (알라딘 api 호출)
    @GetMapping("/api/book/byCategory")
    public ResponseEntity byCategory(@RequestParam Long categoryId, @RequestParam(required = false) String page,
                                     @RequestParam(required = false) String size) {
        return new ResponseEntity(success(bookService.byCategory(categoryId, page, size)), HttpStatus.OK);
    }

    // 해당 도서에 대한 추천 도서 (도서관 정보나루 api)
    @GetMapping("/api/book/recommend")
    public ResponseEntity recommend(@RequestParam Long isbn) {
        return new ResponseEntity(success(bookService.recommend(isbn)), HttpStatus.OK);
    }
}
