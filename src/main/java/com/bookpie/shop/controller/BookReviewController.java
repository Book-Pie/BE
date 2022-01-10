package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.service.BookReviewService;
import com.bookpie.shop.service.ReviewLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;

@Slf4j
@RestController
@RequestMapping("/api/book-review")
public class BookReviewController {
    @Autowired
    private BookReviewService bookReviewService;
    @Autowired
    private ReviewLikeService reviewLikeService;

    // 도서 리뷰 작성
    @PostMapping("")
    public ResponseEntity create(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.create(dto)), HttpStatus.OK);
    }

    // 도서 리뷰 수정
    @PutMapping("")
    public ResponseEntity update(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.update(dto)), HttpStatus.OK);
    }

    // 도서 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity delete(@PathVariable Long reviewId) {
        return new ResponseEntity(success(bookReviewService.delete(reviewId)), HttpStatus.OK);
    }

    // 도서 리뷰에 좋아요(등록/취소)
    @GetMapping("/like/{reviewId}")
    public ResponseEntity likeUp(@PathVariable Long reviewId) {
        return new ResponseEntity(success(reviewLikeService.like(reviewId)), HttpStatus.OK);
    }

    // 해당 도서에 대한 도서 리뷰 조회
    @GetMapping("/{isbn}")
    public ResponseEntity getReview(@PathVariable Long isbn, @RequestParam(required = false) String page,
                                    @RequestParam(required = false) String size, @RequestParam Long userId) {
        return new ResponseEntity(success(bookReviewService.getReview(isbn, page, size, userId)), HttpStatus.OK);
    }

    // 내가 쓴 도서리뷰 조회
    @GetMapping("/my")
    public ResponseEntity getMyReview(@RequestParam(required = false) String page,
                                      @RequestParam(required = false) String size, @RequestParam(required = false) Long userId) {
        return new ResponseEntity(success(bookReviewService.getMyReview(userId, page, size)), HttpStatus.OK);
    }

}
