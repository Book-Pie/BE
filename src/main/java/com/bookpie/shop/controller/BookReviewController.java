package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.service.BookReviewService;
import com.bookpie.shop.service.ReviewLikeService;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;
@RestController
@RequestMapping("/api/book-review")
public class BookReviewController {
    @Autowired
    private BookReviewService bookReviewService;
    @Autowired
    private ReviewLikeService reviewLikeService;

    // 도서 리뷰 작성
    @PostMapping("/create")
    public ApiResult create(@RequestBody BookReviewDto dto) {
        return success(bookReviewService.create(dto));
    }

    // 도서 리뷰 수정
    @PutMapping("/update")
    public ApiResult update(@RequestBody BookReviewDto dto) {
        return success(bookReviewService.update(dto));
    }

    // 도서 리뷰 삭제
    @DeleteMapping("/delete/{review_id}")
    public ApiResult delete(@PathVariable Long review_id) {
        return success(bookReviewService.delete(review_id));
    }

    // 도서 리뷰에 좋아요(등록/취소)
    @GetMapping("/like/{review_id}")
    public ApiResult likeUp(@PathVariable Long review_id) {
        return success(reviewLikeService.like(review_id));
    }

    // 해당 도서에 대한 도서 리뷰 조회
    @GetMapping("/getReview/{book_id}")
    public ApiResult getReview(@PathVariable Long book_id, @RequestParam int page, @RequestParam int size) {
        return success(bookReviewService.getReview(book_id, page, size));
    }

    // 내가 쓴 도서리뷰 조회
    @GetMapping("/my/{user_id}")
    public ApiResult getMyReview(@PathVariable Long user_id, @RequestParam int page, @RequestParam int size) {
        return success(bookReviewService.getMyReview(user_id, page, size));
    }

}
