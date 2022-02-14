package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.reply.BoardReplyDto;
import com.bookpie.shop.domain.dto.reply.SubReplyDto;
import com.bookpie.shop.domain.dto.reply.UsedBookReplyDto;
import com.bookpie.shop.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.bookpie.shop.utils.ApiUtil.success;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
@Slf4j
public class ReplyController {
    private final ReplyService replyService;

    // 게시글에 댓글 작성
    @PostMapping("/board")
    public ResponseEntity create(@RequestBody BoardReplyDto dto) {
        return new ResponseEntity(success(replyService.create(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글에 댓글 수정
    @PutMapping("/board")
    public ResponseEntity update(@RequestBody BoardReplyDto dto) {
        return new ResponseEntity(success(replyService.update(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 댓글 삭제
    @DeleteMapping("/board/{replyId}")
    public ResponseEntity delete(@PathVariable Long replyId) {
        return new ResponseEntity(success(replyService.delete(replyId, getCurrentUserId())), HttpStatus.OK);
    }

    // 게시글 댓글 조회 (페이징 기능)
    @GetMapping("/board/{boardId}")
    public ResponseEntity boardReplyList(@PathVariable Long boardId,
                                         @RequestParam(required = false, defaultValue = "0") String page,
                                         @RequestParam(required = false, defaultValue = "10") String size) {
        return new ResponseEntity(success(replyService.getAll(boardId, page, size)), HttpStatus.OK);
    }

    // 대댓글 달기
    @PostMapping("")
    public ResponseEntity createSubReply(@RequestBody SubReplyDto dto) {
        return new ResponseEntity(success(replyService.createSubReply(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 대댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity deleteSubReply(@PathVariable Long replyId) {
        return new ResponseEntity(success(replyService.deleteSubReply(replyId, getCurrentUserId())), HttpStatus.OK);
    }

    // 대댓글 수정
    @PutMapping("")
    public ResponseEntity updateSubReply(@RequestBody BoardReplyDto dto) {
        return new ResponseEntity(success(replyService.updateSubReply(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 중고도서 댓글 작성
    @PostMapping("/usedbook")
    public ResponseEntity replyOnUsedBook(@RequestBody UsedBookReplyDto dto) {
        return new ResponseEntity(success(replyService.replyOnUsedBook(dto, getCurrentUserId())), HttpStatus.OK);
    }

    // 중고도서 댓글 수정
    @PutMapping("/usedbook")
    public ResponseEntity updateReplyOnUsedBook(@RequestBody UsedBookReplyDto dto) {
        return new ResponseEntity(success(replyService.updateReplyOnUsedBook(dto, getCurrentUserId())), HttpStatus.OK);
    }
    // 중고도서 댓글 삭제
    @DeleteMapping("/usedbook/{replyId}")
    public ResponseEntity deleteReplyOnUsedBook(@PathVariable Long replyId) {
        return new ResponseEntity(success(replyService.deleteReplyOnUsedBook(replyId, getCurrentUserId())), HttpStatus.OK);
    }

    // 중고도서 댓글 리스트 조회
    @GetMapping("/usedbook/{usedBookId}")
    public ResponseEntity usedBookReplyList(@PathVariable Long usedBookId,
                                            @RequestParam(required = false, defaultValue = "0") String page,
                                            @RequestParam(required = false, defaultValue = "10") String size) {
        return new ResponseEntity(success(replyService.usedBookReplyList(usedBookId, page, size)), HttpStatus.OK);
    }
    private Long getCurrentUserId(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getId();
        }catch (Exception e){
            throw new ClassCastException("토큰에서 사용자 정보를 불러오는데 실패하였습니다.");
        }
    }

}
