package com.bookpie.shop.service;

import com.bookpie.shop.domain.Book;
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
import java.util.Optional;

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
    public boolean create(BookReviewDto dto) {
        // 책 조회 및 예외 발생
        Book book = bookRepository.findById(dto.getBook_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 책은 존재하지 않습니다."));
        // 회원 객체 생성
        Optional<User> user = userRepository.findById(dto.getUser_id());
        User objUser = user.orElse(null);

        // 책 리뷰 엔티티 생성
        BookReview bookReview = BookReview.createBookReview(dto, book, objUser);

        // 연관 관계 생성
        book.getReviews().add(bookReview);
        objUser.getBookReviews().add(bookReview);


        // DB저장
        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        if (createdBookReview == null) return false;
        return true;
    }

    // 도서 리뷰 수정
    public boolean update(BookReviewDto dto) {
        // 해당 리뷰 생성
        BookReview bookReview = bookReviewRepository.findById(dto.getReview_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));
        // 리뷰 수정
        bookReview.patch(dto);
        //
        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        if (createdBookReview == null) return false;
        return true;
    }

    // 도서 리뷰 삭제
    public boolean delete(Long review_id) {
        // 해당 리뷰 생성
        BookReview bookReview = bookReviewRepository.findById(review_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));
        bookReviewRepository.delete(bookReview);
        return true;
    }

    // 해당 도서의 리뷰 전체 조회
    public Page<BookReviewDto> getReview(Long book_id, String page, String size) {
        log.info("page와 size : " + page + ", " + size);

        // 도서 확인 및 예외 발생
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서는 존재하지 않습니다."));
        // 임시 유저
        Long user_id = 1L;

        // page와 size 값을 int로 변환 (디폴트값 동시 설정)
        int realPage = 0;
        int realSize = 4;

        // page, size 디폴트값 설정
        if (page != null) { realPage = Integer.parseInt(page); }

        if (size != null) { realSize = Integer.parseInt(size); }


        PageRequest pageRequest = PageRequest.of(realPage, realSize, Sort.by("reviewDate").descending());  // 페이징 정보
        Page<BookReview> bookReviewPage = bookReviewRepository.findAll(pageRequest);  // 페이징 처리

        return bookReviewPage.map(bookReview -> BookReviewDto.createBookReviewDto(bookReview, user_id));
    }


    // 내가 쓴 도서리뷰
    public Page<BookReviewDto> getMyReview(Long user_id, String page, String size) {
        // 회원 객체 생성
        Optional<User> user = userRepository.findById(user_id);
        User objUser = user.orElse(null);

        // page, size 값 디폴트
        int realPage = 0;
        int realSize = 5;

        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        // 페이징 데이터
        Pageable pageable = PageRequest.of(realPage, realSize, Sort.by("reviewDate").descending());

        // 해당 회원이 작성한 도서 리뷰 조회
        Page<BookReview> bookReviewPage = bookReviewRepository.findAllByUserId(user_id, pageable);


        List<BookReview> reviewList = objUser.getBookReviews();

        return bookReviewPage.map(bookReview -> BookReviewDto.createBookReviewDto(bookReview, user_id));
    }
}
