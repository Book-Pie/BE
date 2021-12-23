package com.bookpie.shop.service;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.repository.BoardRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    // 게시글 작성
    public boolean create(BoardDto dto) {
        // 유저 객체 생성
        Optional<User> user = userRepository.findById(dto.getUser_id());
        // user 객체를 꺼내기 위한 메서드
        User createdUser = user.orElse(null);

        // 게시글 엔티티 생성
        Board board = Board.createBoard(dto, createdUser);
        log.info("Service board : " + board);

        // 게시글 DB에 저장
        Board createdBoard = boardRepository.save(board);

        // 연관관계 매핑
        List<Board> boardList = createdUser.getBoards();
        boardList.add(createdBoard);

        if (createdBoard == null) return false;
        return true;
    }

    // 게시글 수정
    public boolean update(BoardDto dto) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(dto.getBoard_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 게시글 수정
        board.patch(dto);

        // DB 저장
        Board updatedBoard = boardRepository.save(board);
        if (updatedBoard == null) return false;
        return true;
    }

    // 게시글 삭제
    public boolean delete(Long board_id) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        // 게시글 삭제
        boardRepository.delete(board);

        return true;
    }

    // 게시글 전체 조회(카테고리별)
    public List<BoardDto> getAll(String boardType) {
        log.info("Service boardType : " + boardType);
        // 존재하지 않는 카테고리
        if (BoardType.valueOf(boardType) == null)
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");

        // 게시글 조회
//        List<Board> boardList = boardRepository.findByBoardType(boardType);
//
//        // 변환 : 엔티티 -> Dto
//        List<BoardDto> dtos = new ArrayList<>();
//
//        for (int i = 0; i < boardList.size(); i++) {
//            Board b = boardList.get(i);
//            BoardDto dto = BoardDto.createBoardDto(b);
//            dtos.add(dto);
//        }

        return boardRepository.findByBoardType(boardType)
                .stream().map(board -> BoardDto.createBoardDto(board))
                .collect(Collectors.toList());
    }

    // 게시글 상세 보기
    public BoardDto getBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        BoardDto dto = BoardDto.createBoardDto(board);
        return dto;
    }
}
