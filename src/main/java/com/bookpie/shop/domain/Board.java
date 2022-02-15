package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    @Lob
    private String content;
    @Column(name = "board_date")
    private LocalDateTime boardDate;
    @Builder.Default private int view = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    private BoardType boardType;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    public static Board createBoard(BoardDto dto, User user, Long userId) {
        // 예외 처리
        if (!user.getId().equals(userId))
            throw new IllegalArgumentException("게시글 작성 실패! 회원이 일치하지 않습니다.");
        if (dto == null)
            throw new IllegalArgumentException("게시글 작성 실패! 게시글이 존재하지 않습니다.");

        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .boardType(dto.getBoardType())
                .price(dto.getPrice())
                .boardDate(LocalDateTime.now())
                .user(user)
                .build();
    }

    public void patch(BoardDto dto) {
        // 예외 발생
        if (this.id != dto.getBoardId())
            throw new IllegalArgumentException("게시글이 일치하지 않습니다.");

        // 게시글 수정
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getPrice() != this.price) this.price = dto.getPrice();
    }

}
