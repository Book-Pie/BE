package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.service.BookReviewService;
import com.bookpie.shop.service.ReviewLikeService;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.create(dto)), HttpStatus.OK);
    }

    // 도서 리뷰 수정
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.update(dto)), HttpStatus.OK);
    }

    // 도서 리뷰 삭제
    @DeleteMapping("/delete/{review_id}")
    public ResponseEntity delete(@PathVariable Long review_id) {
        return new ResponseEntity(success(bookReviewService.delete(review_id)), HttpStatus.OK);
    }

    // 도서 리뷰에 좋아요(등록/취소)
    @GetMapping("/like/{review_id}")
    public ResponseEntity likeUp(@PathVariable Long review_id) {
        return new ResponseEntity(success(reviewLikeService.like(review_id)), HttpStatus.OK);
    }

    // 해당 도서에 대한 도서 리뷰 조회
    @GetMapping("/getReview/{book_id}")
    public ResponseEntity getReview(@PathVariable Long book_id, @RequestParam(required = false) String page,
                                    @RequestParam(required = false) String size) {
        log.info("도서 리뷰 조회 : " + page + ", " + size);

        return new ResponseEntity(success(bookReviewService.getReview(book_id, page, size)), HttpStatus.OK);
    }

    // 내가 쓴 도서리뷰 조회
    @GetMapping("/my/{user_id}")
    public ResponseEntity getMyReview(@PathVariable Long user_id, @RequestParam(required = false) String page, @RequestParam(required = false) String size) {
        return new ResponseEntity(success(bookReviewService.getMyReview(user_id, page, size)), HttpStatus.OK);
    }

}
