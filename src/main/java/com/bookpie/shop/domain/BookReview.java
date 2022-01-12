package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.book_review.BookReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_review_id")
    private Long id;

    private String content;
    private float rating;
    private Long isbn;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book_review")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    public static BookReview createBookReview(BookReviewDto dto, User user) {
        return BookReview.builder()
                .content(dto.getContent())
                .rating(dto.getRating())
                .isbn(dto.getIsbn())
                .reviewDate(LocalDateTime.now())
                .user(user)
                .build();
    }

    public void patch(BookReviewDto dto) {
        // 예외 발생
        if (this.id != dto.getReviewId())
            throw new IllegalArgumentException("리뷰 id가 일치하지 않습니다.");

        // 객체를 갱신
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getRating() != 0) this.rating = dto.getRating();
    }

}
