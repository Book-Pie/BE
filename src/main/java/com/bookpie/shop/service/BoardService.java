package com.bookpie.shop.service;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.BookReview;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.repository.BoardRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BoardDto create(BoardDto dto) {
        log.info("BoardService create() 실행 : " + dto);

        // 유저 객체 생성
        Optional<User> user = userRepository.findById(dto.getUser_id());
        // user 객체를 꺼내기 위한 메서드
        User createdUser = user.orElse(null);

        log.info("생성된 user : " + createdUser);

        // 게시글 엔티티 생성
        Board board = Board.createBoard(dto, createdUser);

        // 게시글 DB에 저장
        Board createdBoard = boardRepository.save(board);

        // 연관관계 매핑
        createdUser.getBoards().add(createdBoard);

        return BoardDto.createBoardDto(createdBoard);
    }

    // 게시글 수정
    public BoardDto update(BoardDto dto) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(dto.getBoard_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 게시글 수정
        board.patch(dto);

        // DB 저장
        Board updatedBoard = boardRepository.save(board);

        return BoardDto.createBoardDto(updatedBoard);
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
//    public List<BoardDto> getAll(BoardType boardType, int page, int size) {
//        log.info("Service getAll() 실행 : " + boardType);
//        // 존재하지 않는 카테고리
//        if (BoardType.valueOf(boardType.name()) == null)
//            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
//
//        // 게시글 조회
//        Pageable pageable = PageRequest.of(page, size, Sort.by("boardDate").descending());  // 페이징 정보
//        log.info("페이지 정보 : " + pageable.toString());
//        //Page<Board> boardPage = boardRepository.findByBoardType(boardType, pageable);
//
//        return boardRepository.findByBoardType(boardType, pageable)
//                .stream().map(board -> BoardDto.createBoardDto(board))
//                .collect(Collectors.toList());
//    }
    // 게시글 전체 조회(카테고리별), 페이징 포함
    public Page<BoardDto> getAll(BoardType boardType, String page, String size) {
        log.info("Service getAll() 실행 : " + boardType);
        // 존재하지 않는 카테고리
        if (BoardType.valueOf(boardType.name()) == null)
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");

        // page, size 디폴트값
        int realPage = 0;
        int realSize = 10;

        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        // 게시글 조회
        Pageable pageable = PageRequest.of(realPage, realSize, Sort.by("boardDate").descending());  // 페이징 정보
        log.info("페이지 정보 : " + pageable.toString());

        Page<Board> boardPage = boardRepository.findByBoardType(boardType, pageable);
        return boardPage.map(board -> BoardDto.createBoardDto(board));
    }

    // 게시글 상세 보기
    public BoardDto getBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        BoardDto dto = BoardDto.createBoardDto(board);
        return dto;
    }

    // 해당 회원이 작성한 게시글 전체 보기 (페이징 처리함)
    public Page<BoardDto> getMyBoard(Long user_id, String page, String size) {
        Optional<User> user = userRepository.findById(user_id);
        User objUser = user.orElse(null);

        // page, size 디폴트값
        int realPage = 0;
        int realSize = 10;

        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        Pageable pageable = PageRequest.of(realPage, realSize, Sort.by("boardDate").descending());

        // 해당 유저가 작성한 게시글
        Page<Board> boardList = boardRepository.findAllByUserId(objUser.getId(), pageable);

        return boardList.map(board -> BoardDto.createBoardDto(board));
    }

    // 조회수 증가하는 메서드
    public void viewPlus(Long board_id) {
        boardRepository.viewPlus(board_id);
    }
}
