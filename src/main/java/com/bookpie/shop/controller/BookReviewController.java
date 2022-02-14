package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.service.BookReviewService;
import com.bookpie.shop.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import static com.bookpie.shop.utils.ApiUtil.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-review")
public class BookReviewController {
    private final BookReviewService bookReviewService;
    private final ReviewLikeService reviewLikeService;

    // 도서 리뷰 작성
    @PostMapping("")
    public ResponseEntity create(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.create(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 도서 리뷰 수정
    @PutMapping("")
    public ResponseEntity update(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(bookReviewService.update(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 도서 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity delete(@PathVariable Long reviewId) {
        return new ResponseEntity(success(bookReviewService.delete(reviewId, getCurrentUserId())), HttpStatus.OK);
    }

    // 도서 리뷰에 좋아요(등록/취소)
    @PostMapping("/like")
    public ResponseEntity likeUp(@RequestBody BookReviewDto dto) {
        return new ResponseEntity(success(reviewLikeService.like(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 해당 도서에 대한 도서 리뷰 조회
    @GetMapping("/{isbn}")
    public ResponseEntity getReview(@PathVariable String isbn,
                                    @RequestParam(required = false, defaultValue = "0") String page,
                                    @RequestParam(required = false, defaultValue = "4") String size) {

        return new ResponseEntity(success(bookReviewService.getReview(isbn, page, size)), HttpStatus.OK);
    }

    // 내가 쓴 도서리뷰 조회
    @GetMapping("/myReview/{userId}")
    public ResponseEntity getMyReview(@RequestParam(required = false, defaultValue = "0") String page,
                                      @RequestParam(required = false, defaultValue = "5") String size,
                                      @PathVariable Long userId) {
        return new ResponseEntity(success(bookReviewService.getMyReview(userId, page, size)), HttpStatus.OK);
    }

    // 해당 도서에 내가 쓴 도서리뷰
    @GetMapping("/my/{isbn}")
    public ResponseEntity myReview(@PathVariable String isbn) {
        return new ResponseEntity(success(bookReviewService.myReview(isbn, getCurrentUserId())), HttpStatus.OK);
    }

    // 해당 도서에서 베스트 리뷰 2개
    @GetMapping("/bestReview/{isbn}")
    public ResponseEntity bestReview(@PathVariable String isbn) {
        return new ResponseEntity(success(bookReviewService.bestReview(isbn)), HttpStatus.OK);
    }

    // 회원이 가장 많은 리뷰를 남긴 카테고리 top 5
    @GetMapping("/myCategory/{userId}")
    public ResponseEntity myCategory(@PathVariable Long userId) {
        return new ResponseEntity(success(bookReviewService.myCategory(userId)), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getId();
        }catch (Exception e){
            throw new ClassCastException("토큰에서 사용자 정보를 불러오는데 실패하였습니다.");
        }
    }
}
