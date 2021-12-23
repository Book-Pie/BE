package com.bookpie.shop.domain.dto.board;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.enums.BoardType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BoardDto {
    // 게시글 작성 시 필요한 값
    private Long board_id;
    private String title;
    private String content;
    private int price;
    private BoardType boardType;

    // 게시글 출력 시 필요한 값 (작성 시 필요한 값들과 함께 전달돼야됨)
    private Long user_id;
    private String nickname;
    private LocalDateTime boardDate;
    private int view;

//    public BoardDto(Long board_id, String title, String content, int price, BoardType boardType,
//                    Long user_id, String nickName, LocalDateTime boardDate, int view) {
//        this.board_id = board_id;
//        this.title = title;
//        this.content = content;
//        this.price = price;
//        this.boardType = boardType;
//        this.user_id = user_id;
//        this.nickname = nickName;
//        this.boardDate = boardDate;
//        this.view = view;
//    }


    public static BoardDto createBoardDto(Board board) {
        return new BoardDto(board.getId(), board.getTitle(), board.getContent(),
                board.getPrice(), board.getBoardType(), board.getUser().getId(),
                board.getUser().getNickName(), board.getBoardDate(), board.getView());
    }
}
