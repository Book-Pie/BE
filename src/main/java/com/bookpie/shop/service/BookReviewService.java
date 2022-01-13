package com.bookpie.shop.service;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import com.bookpie.shop.repository.BookRepository;
import com.bookpie.shop.repository.BookReviewRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class BookReviewService {
    @Autowired
    private BookReviewRepository bookReviewRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    // 도서 리뷰 작성
    public BookReviewDto create(BookReviewDto dto) {
        // 회원 객체 생성
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));


        // 책 리뷰 엔티티 생성
        BookReview bookReview = BookReview.createBookReview(dto, user);

        // 연관 관계 생성
        user.getBookReviews().add(bookReview);

        // DB저장
        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        return BookReviewDto.createDto(createdBookReview, user.getId());
    }

    // 도서 리뷰 수정
    public BookReviewDto update(BookReviewDto dto) {
        // 해당 리뷰 생성
        BookReview bookReview = bookReviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));

        // 리뷰 수정
        bookReview.patch(dto);

        // 리뷰 수정 후 DB저장
        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        return BookReviewDto.createDto(createdBookReview, dto.getUserId());
    }

    // 도서 리뷰 삭제
    public String delete(Long reviewId) {
        // 해당 리뷰 생성
        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));
        bookReviewRepository.delete(bookReview);
        return "해당 리뷰가 삭제되었습니다.";
    }

    // 해당 도서의 리뷰 전체 조회
    public Page<BookReviewDto> getReview(Long isbn, String page, String size, String userId) {
        Long user_id = 0L;
        if (userId != null) user_id = Long.parseLong(userId);

        // page와 size 값을 int로 변환 (디폴트값 동시 설정)
        int realPage = 0;
        int realSize = 4;

        // page, size 디폴트값 설정
        if (page != null) { realPage = Integer.parseInt(page); }

        if (size != null) { realSize = Integer.parseInt(size); }


        PageRequest pageRequest = PageRequest.of(realPage, realSize, Sort.by("reviewDate").descending());  // 페이징 정보
        Page<BookReview> bookReviewPage = bookReviewRepository.findAllByIsbn(isbn, pageRequest);  // 페이징 처리

        Long finalUser_id = user_id;
        return bookReviewPage.map(bookReview -> BookReviewDto.createDto(bookReview, finalUser_id));
    }


    // 내가 쓴 도서리뷰
    public Page<BookReviewDto> getMyReview(Long userId, String page, String size) {
        // 회원 객체 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        // page, size 값 디폴트
        int realPage = 0;
        int realSize = 5;

        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        // 페이징 데이터
        Pageable pageable = PageRequest.of(realPage, realSize, Sort.by("reviewDate").descending());

        // 해당 회원이 작성한 도서 리뷰 조회
        Page<BookReview> bookReviewPage = bookReviewRepository.findAllByUserId(userId, pageable);


        List<BookReview> reviewList = user.getBookReviews();

        return bookReviewPage.map(bookReview -> BookReviewDto.createDto(bookReview, userId));
    }
}
