package com.bookpie.shop.service;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.repository.BoardRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
        // 유저 유효성 검사
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        // 게시글 엔티티 생성
        Board board = Board.createBoard(dto, user);

        // 게시글 DB에 저장
        Board createdBoard = boardRepository.save(board);

        // 연관관계 매핑
        user.getBoards().add(createdBoard);

        return BoardDto.createBoardDto(createdBoard);
    }

    // 게시글 수정
    public BoardDto update(BoardDto dto) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 게시글 수정
        board.patch(dto);

        // DB 저장
        Board updatedBoard = boardRepository.save(board);

        return BoardDto.createBoardDto(updatedBoard);
    }

    // 게시글 삭제
    public boolean delete(Long boardId) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        // 게시글 삭제
        boardRepository.delete(board);

        return true;
    }

    // 게시글 전체 조회(카테고리별), 페이징 포함
    public Page<BoardDto> getAll(BoardType boardType, String page, String size) {
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

        Page<Board> boardPage = boardRepository.findByBoardType(boardType, pageable);
        return boardPage.map(board -> BoardDto.createBoardDto(board));
    }

    // 게시글 상세 보기
    public BoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        BoardDto dto = BoardDto.createBoardDto(board);
        return dto;
    }

    // 해당 회원이 작성한 게시글 전체 보기 (페이징 처리함)
    public Page<BoardDto> getMyBoard(Long userId, String page, String size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        // page, size 디폴트값
        int realPage = 0;
        int realSize = 10;

        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        Pageable pageable = PageRequest.of(realPage, realSize, Sort.by("boardDate").descending());

        // 해당 유저가 작성한 게시글
        Page<Board> boardList = boardRepository.findAllByUserId(user.getId(), pageable);

        return boardList.map(board -> BoardDto.createBoardDto(board));
    }

    // 조회수 증가하는 메서드
    public void viewPlus(Long boardId) {
        boardRepository.viewPlus(boardId);
    }
}
