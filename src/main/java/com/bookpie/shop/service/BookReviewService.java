package com.bookpie.shop.service;

import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.book_review.BookReviewDto;
import com.bookpie.shop.repository.BookReviewRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final UserRepository userRepository;

    // 도서 리뷰 작성
    public BookReviewDto create(BookReviewDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        BookReview checkBookReview = bookReviewRepository.findMyReview(dto.getIsbn(), userId);
        if (checkBookReview != null)
            throw new IllegalArgumentException("도서 리뷰는 한 번 밖에 작성할 수 없습니다.");

        BookReview bookReview = BookReview.createBookReview(dto, user);

        user.getBookReviews().add(bookReview);

        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        return BookReviewDto.createDto(createdBookReview, user.getId());
    }

    // 도서 리뷰 수정
    public BookReviewDto update(BookReviewDto dto, Long userId) {
        BookReview bookReview = bookReviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));

        if (!bookReview.getUser().getId().equals(userId))
            throw new IllegalArgumentException("도서리뷰 수정 실패! 회원 정보가 일치하지 않습니다.");

        bookReview.patch(dto);

        BookReview createdBookReview = bookReviewRepository.save(bookReview);
        return BookReviewDto.createDto(createdBookReview, userId);
    }

    // 도서 리뷰 삭제
    public String delete(Long reviewId, Long userId) {
        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));

        if (!bookReview.getUser().getId().equals(userId))
            throw new IllegalArgumentException("도서리뷰 삭제 실패! 회원 정보가 일치하지 않습니다.");

        bookReviewRepository.delete(bookReview);
        return "해당 리뷰가 삭제되었습니다.";
    }

    // 해당 도서의 리뷰 전체 조회
    public JSONObject getReview(String isbn, String page, String size, Long userId) {
        JSONObject response = new JSONObject();

        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("reviewDate").descending());  // 페이징 정보

        Long finalUser_id = userId;

        Page<BookReview> bookReviews = bookReviewRepository.findAllByIsbn(isbn, pageRequest);
        // 해당 isbn에 등록된 리뷰가 없으면 빈 배열 보내기
        if (bookReviews.isEmpty()) {
            response.put("content", bookReviews.stream().toArray());
            response.put("pageable", bookReviews.getPageable());
            response.put("last", bookReviews.isLast());
            response.put("totalElements", bookReviews.getTotalElements());
            response.put("totalPages", bookReviews.getTotalPages());
            response.put("first", bookReviews.isFirst());
            response.put("empty", bookReviews.isEmpty());
        } else {
            Page<BookReviewDto> bookReviewDtoPage = bookReviews.map(bookReview -> BookReviewDto.createDto(bookReview, finalUser_id));

            Double averageRating = bookReviewRepository.averageRating(isbn);
            averageRating = Math.round(averageRating * 100) / 100.0;

            response.put("content", bookReviewDtoPage.stream().toArray());
            response.put("pageable", bookReviewDtoPage.getPageable());
            response.put("last", bookReviewDtoPage.isLast());
            response.put("totalElements", bookReviewDtoPage.getTotalElements());
            response.put("totalPages", bookReviewDtoPage.getTotalPages());
            response.put("first", bookReviewDtoPage.isFirst());
            response.put("empty", bookReviewDtoPage.isEmpty());
            response.put("averageRating", averageRating);

            // 해당 책에 내가 작성한 도서리뷰가 있는지 확인하고 있으면 true, 없으면 false
            if (myReview(isbn, userId) != null) {
                response.put("myCommentCheck", true);
            } else {
                response.put("myCommentCheck", false);
            }
        }

        return response;
    }


    // 내가 쓴 도서리뷰
    public Page<BookReviewDto> getMyReview(Long userId, String page, String size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("reviewDate").descending());

        Page<BookReview> bookReviewPage = bookReviewRepository.findAllByUserId(userId, pageable);

        return bookReviewPage.map(bookReview -> BookReviewDto.createDto(bookReview, userId));
    }

    // 해당 책에 내가 쓴 도서리뷰
    public BookReviewDto myReview(String isbn, Long userId) {
        if (userId.equals(0L)) {
            return null;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        BookReview bookReview = bookReviewRepository.findMyReview(isbn, userId);
        if (bookReview == null) return null;

        return BookReviewDto.createDto(bookReview, userId);
    }

    // 해당 책에서 베스트 리뷰 2개
    public List<BookReviewDto> bestReview(String isbn, Long userId) {
        List<BookReview> reviewList = bookReviewRepository.bestReview(isbn);
        Long finalUser_id = userId;
        return reviewList.stream()
                .map(bookReview -> BookReviewDto.createDto(bookReview, finalUser_id))
                .collect(Collectors.toList());
    }

    public List<JSONObject> myCategory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<JSONObject> myCategoryList = bookReviewRepository.myCategory(userId);

        List<JSONObject> resultObjs = new ArrayList<>();

        for (int i = 0; i < myCategoryList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", myCategoryList.get(i).get("category"));
            jsonObject.put("label", myCategoryList.get(i).get("category"));
            jsonObject.put("value", myCategoryList.get(i).get("count"));
            resultObjs.add(jsonObject);
        }

        return resultObjs;
    }

}
