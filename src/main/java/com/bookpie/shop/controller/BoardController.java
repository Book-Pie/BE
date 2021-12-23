package com.bookpie.shop.controller;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.service.BoardService;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookpie.shop.utils.ApiUtil.error;
import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("/create")
    public ApiResult create(@RequestBody BoardDto dto) {
        return success(boardService.create(dto));
    }

    // 게시글 수정
    @PutMapping("/update")
    public ApiResult update(@RequestBody BoardDto dto) {
        return success(boardService.update(dto));
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{board_id}")
    public ApiResult delete(@PathVariable Long board_id) {
        return success(boardService.delete(board_id));
    }

    // 게시글 전체 조회(카테고리별)
    @GetMapping("/getAll/{boardType}")
    public ApiResult getAll(@PathVariable String boardType) {
        return success(boardService.getAll(boardType));
    }

    // 게시글 상세 조회
    @GetMapping("/getBoard/{board_id}")
    public ApiResult get(@PathVariable Long board_id) {
        return success(boardService.getBoard(board_id));
    }
}
