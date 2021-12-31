package com.bookpie.shop.controller;

import com.bookpie.shop.domain.dto.board.BoardDto;
import com.bookpie.shop.domain.enums.BoardType;
import com.bookpie.shop.service.BoardService;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.create(dto)), HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.update(dto)), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{board_id}")
    public ResponseEntity delete(@PathVariable Long board_id) {
        return new ResponseEntity(success(boardService.delete(board_id)), HttpStatus.OK);
    }

    // 게시글 전체 조회(카테고리별)
    @GetMapping("/getAll/{boardType}")
    public ResponseEntity getAll(@PathVariable BoardType boardType, @RequestParam(required = false) String page,
                                 @RequestParam(required = false) String size) {
        return new ResponseEntity(success(boardService.getAll(boardType, page, size)), HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/getBoard/{board_id}")
    public ResponseEntity get(@PathVariable Long board_id, HttpServletRequest req,
                         HttpServletResponse rep) {
        Cookie oldCookie = null;
        Cookie cookies[] = req.getCookies();

        if (cookies != null) {
            log.info("쿠키가 존재합니다.");
            for (Cookie  cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) {
            log.info("oldCookie가 존재합니다.");
            //oldCookie가 존재하지만 해당 번호를 가지지 않았을 경우 조회수 +1
            if (!oldCookie.getValue().contains("[" + board_id + "]")) {
                boardService.viewPlus(board_id);
                oldCookie.setValue(oldCookie.getValue()+"_["+board_id+"]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                rep.addCookie(oldCookie);
            }
        }else {
            // oldCookie 존재하지 않을 경우 쿠키 새로 만들고 조회수 + 1
            log.info("oldCookie가 존재하지 않습니다.");
            Cookie newCookie = new Cookie("postView", "["+board_id+"}");
            boardService.viewPlus(board_id);
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            rep.addCookie(newCookie);
        }
        return new ResponseEntity(success(boardService.getBoard(board_id)), HttpStatus.OK);
    }

    // 회원이 작성한 게시글 보기
    @GetMapping("/my/{user_id}")
    public ResponseEntity myBoard(@PathVariable Long user_id, @RequestParam(required = false) String page,
                                  @RequestParam(required = false) String size) {
        return new ResponseEntity(success(boardService.getMyBoard(user_id, page, size)), HttpStatus.OK);
    }
}
