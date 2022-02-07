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
    @PostMapping("")
    public ResponseEntity create(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.create(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("")
    public ResponseEntity update(@RequestBody BoardDto dto) {
        return new ResponseEntity(success(boardService.update(dto)), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity delete(@PathVariable Long boardId) {
        return new ResponseEntity(success(boardService.delete(boardId)), HttpStatus.OK);
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
    public ResponseEntity get(@PathVariable Long boardId,HttpServletRequest req,HttpServletResponse rep) {
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
            if (!oldCookie.getValue().contains("[" + boardId + "]")) {
                boardService.viewPlus(boardId);
                oldCookie.setValue(oldCookie.getValue()+"_["+boardId+"]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                rep.addCookie(oldCookie);
            }
        }else {
            // oldCookie 존재하지 않을 경우 쿠키 새로 만들고 조회수 + 1
            log.info("oldCookie가 존재하지 않습니다.");
            Cookie newCookie = new Cookie("postView", "["+boardId+"}");
            boardService.viewPlus(boardId);
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            rep.addCookie(newCookie);
        }
        return new ResponseEntity(success(boardService.getBoard(boardId)), HttpStatus.OK);
    }

    // 회원이 작성한 게시글 보기
    @GetMapping("/my/{userId}")
    public ResponseEntity myBoard(@PathVariable Long userId,
                                  @RequestParam(required = false, defaultValue = "0") String page,
                                  @RequestParam(required = false, defaultValue = "10") String size) {
        return new ResponseEntity(success(boardService.getMyBoard(userId, page, size)), HttpStatus.OK);
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
