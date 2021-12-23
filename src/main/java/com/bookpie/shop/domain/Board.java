package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;
    @Column(name = "board_date")
    private LocalDateTime boardDate;
    private int view;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    private BoardType boardType;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    public Board(String title, String content, LocalDateTime now, int view, BoardType boardType, int price, User user) {
        this.title = title;
        this.content = content;
        this.boardDate = now;
        this.view = view;
        this.boardType = boardType;
        this.price = price;
        this.user = user;
    }

    public static Board createBoard(BoardDto dto, User user) {
        // 예외 처리
        if (dto.getUser_id() != user.getId())
            throw new IllegalArgumentException("게시글 작성 실패! 회원이 일치하지 않습니다.");
        if (dto == null)
            throw new IllegalArgumentException("게시글 작성 실패! 게시글이 존재하지 않습니다.");

        return new Board(dto.getTitle(), dto.getContent(),
                LocalDateTime.now(), 0, dto.getBoardType(), dto.getPrice(), user);
    }

    public void patch(BoardDto dto) {
        // 예외 발생
        if (this.id != dto.getBoard_id())
            throw new IllegalArgumentException("게시글이 일치하지 않습니다.");

        // 게시글 수정
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getPrice() != this.price) this.price = dto.getPrice();
    }
}
