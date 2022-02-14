package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("")
    public ResponseEntity create(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.create(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("")
    public ResponseEntity update(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.update(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity delete(@PathVariable Long boardId) {
        return new ResponseEntity(success(boardService.delete(boardId, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 전체 조회(카테고리별)
    @GetMapping("/getAll")
    public ResponseEntity getAll(@RequestParam BoardType boardType,
                                 @RequestParam(required = false, defaultValue = "0") String page,
                                 @RequestParam(required = false, defaultValue = "10") String size) {
        return new ResponseEntity(success(boardService.getAll(boardType, page, size)), HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity get(@PathVariable Long boardId) {
        boardService.viewPlus(boardId);
        return new ResponseEntity(success(boardService.getBoard(boardId)), HttpStatus.OK);
    }

    // 회원이 작성한 게시글 보기
    @GetMapping("/my")
    public ResponseEntity myBoard(@RequestParam(required = false, defaultValue = "0") String page,
                                  @RequestParam(required = false, defaultValue = "10") String size) {
        return new ResponseEntity(success(boardService.getMyBoard(getCurrentUserId(), page, size)), HttpStatus.OK);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity search(@RequestParam(required = false, defaultValue = "") String keyWord,
                                 @RequestParam(required = false, defaultValue = "0") String page,
                                 @RequestParam(required = false, defaultValue = "10") String size,
                                 @RequestParam(required = false) BoardType boardType) {
        return new ResponseEntity(success(boardService.search(keyWord, page, size, boardType)), HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
