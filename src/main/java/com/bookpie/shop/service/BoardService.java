package com.bookpie.shop.service;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.repository.BoardRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    public BoardDto create(BoardDto dto, Long userId) {
        // 유저 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 게시글 엔티티 생성
        Board board = Board.createBoard(dto, user, userId);

        // 게시글 DB에 저장
        Board createdBoard = boardRepository.save(board);

        // 연관관계 매핑
        user.getBoards().add(createdBoard);

        return BoardDto.createBoardDto(createdBoard);
    }

    // 게시글 수정
    public BoardDto update(BoardDto dto, Long userId) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        // 게시글 수정
        board.patch(dto, userId);

        // DB 저장
        Board updatedBoard = boardRepository.save(board);

        return BoardDto.createBoardDto(updatedBoard);
    }

    // 게시글 삭제
    public boolean delete(Long boardId, Long userId) {
        // 게시글 조회 및 예외 발생
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        if (userId != board.getUser().getId())
            throw new IllegalArgumentException("게시글 삭제 실패! 작성한 유저가 아닙니다.");

        // 게시글 삭제
        boardRepository.delete(board);

        return true;
    }

    // 게시글 전체 조회(카테고리별), 페이징 포함
    public Page<BoardDto> getAll(BoardType boardType, String page, String size) {
        // 존재하지 않는 카테고리
        if (BoardType.valueOf(boardType.name()) == null)
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");

        // 게시글 조회
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("boardDate").descending());  // 페이징 정보

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
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("boardDate").descending());

        // 해당 유저가 작성한 게시글
        Page<Board> boardList = boardRepository.findAllByUserId(user.getId(), pageable);

        return boardList.map(board -> BoardDto.createBoardDto(board));
    }

    // 조회수 증가하는 메서드
    public void viewPlus(Long boardId) {
        boardRepository.viewPlus(boardId);
    }

    // 제목으로 검색
    public Page<BoardDto> search(String keyWord, String page, String size, BoardType boardType) {
        String type = boardType.name();
        keyWord = keyWord.replaceAll(" ", "");
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("board_date").descending());
        Page<Board> boardPage = boardRepository.findByKeyword(keyWord, type, pageable);

        return boardPage.map(board -> BoardDto.createBoardDto(board));
    }
}
