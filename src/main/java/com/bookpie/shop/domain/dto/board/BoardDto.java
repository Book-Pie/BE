package com.bookpie.shop.domain.dto.board;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.enums.BoardType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BoardDto {
    // 게시글 작성 시 필요한 값
    private Long boardId;
    private String title;
    private String content;
    private int price;
    private BoardType boardType;

    // 게시글 출력 시 필요한 값 (작성 시 필요한 값들과 함께 전달돼야됨)
    private Long userId;
    private String nickName;
    private LocalDateTime boardDate;
    private int view;

    public static BoardDto createBoardDto(Board board) {
        return new BoardDto(board.getId(), board.getTitle(), board.getContent(),
                board.getPrice(), board.getBoardType(), board.getUser().getId(),
                board.getUser().getNickName(), board.getBoardDate(), board.getView());
    }
}
