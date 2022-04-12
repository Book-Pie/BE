package com.bookpie.shop.service;

import com.bookpie.shop.domain.Board;
import com.bookpie.shop.domain.Reply;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.reply.BoardReplyDto;
import com.bookpie.shop.dto.reply.SubReplyDto;
import com.bookpie.shop.dto.reply.UsedBookReplyDto;
import com.bookpie.shop.repository.BoardRepository;
import com.bookpie.shop.repository.ReplyRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;
    private final BoardRepository boardRepository;

    // 게시글에 댓글 작성
    public BoardReplyDto create(BoardReplyDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(()->new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        Reply reply = Reply.createReplyBoard(dto, user, board);

        if (replyRepository.save(reply) != null) {
            user.getReplies().add(reply);
            board.getReplies().add(reply);
        } else {
            throw new IllegalArgumentException("게시글에 댓글 저장 실패");
        }

        return BoardReplyDto.createReplyDto(reply, reply.getSubReply());
    }

    // 게시글 댓글 수정
    public BoardReplyDto update(BoardReplyDto dto, Long userId) {
        Reply reply = replyRepository.findById(dto.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (!reply.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글 수정 실패! 회원 정보가 일치하지 않습니다.");
        }

        reply.patch(dto);

        return BoardReplyDto.createReplyDto(replyRepository.save(reply), reply.getSubReply());
    }

    // 게시글 댓글 삭제
   public String delete(Long replyId, Long userId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (!reply.getUser().getId().equals(userId)) throw new IllegalArgumentException("게시글 삭제 실패! 회원 정보가 일치하지 않습니다.");

        if (reply.getUsedBook() != null) throw new IllegalArgumentException("중고도서에 대한 댓글입니다.");

        replyRepository.delete(reply);
        return "댓글 삭제 완료 " + reply.getContent();
    }

    // 게시글의 댓글 리스트 조회 (페이징 기능)
    public Page<BoardReplyDto> getAll(Long boardId, String page, String size) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("reply_date").descending());

        Page<Reply> replies = replyRepository.findAllByBoard(boardId, pageable);

        return replies.map(reply -> BoardReplyDto.createDto(reply));
    }

    // 대댓글 작성
    public SubReplyDto createSubReply(SubReplyDto dto, Long userId) {
        Reply reply = replyRepository.findById(dto.getParentReplyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Reply subReply = null;

        if (reply.getSecret()) { // 해당 댓글이 비밀댓글이면
            subReply = Reply.createSubReplyBoard(dto, user, reply, true);
        } else {
            subReply = Reply.createSubReplyBoard(dto, user, reply, false);
        }

        if (replyRepository.save(subReply) != null){
            reply.getSubReply().add(subReply);
            user.getReplies().add(subReply);
        } else {
            throw new IllegalArgumentException("게시판에 대댓글 작성 실패");
        }

        return SubReplyDto.createDto(subReply);
    }

    // 대댓글 삭제
    public String deleteSubReply(Long replyId, Long userId) {
        Reply subReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대댓글은 존재하지 않습니다."));

        if (!subReply.getUser().getId().equals(userId))
            throw new IllegalArgumentException("대댓글 삭제 실패! 회원 정보가 일치하지 않습니다.");

        if (subReply.getParentReply() == null) {
            throw new IllegalArgumentException("대댓글이 아닙니다.");
        }

        replyRepository.delete(subReply);
        return "대댓글 삭제 완료 " + subReply.getContent();
    }
    // 대댓글 수정
    public SubReplyDto updateSubReply(BoardReplyDto dto, Long userId) {
        Reply subReply = replyRepository.findById(dto.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 대댓글은 존재하지 않습니다."));

        if (!subReply.getUser().getId().equals(userId))
            throw new IllegalArgumentException("대댓글 수정 실패! 회원 정보가 일치하지 않습니다.");

        if (subReply.getParentReply() == null) {
            throw new IllegalArgumentException("대댓글이 아닙니다.");
        }

        subReply.patch(dto);
        return SubReplyDto.createDto(subReply);
    }

    // 중고도서에 댓글 작성
    public UsedBookReplyDto replyOnUsedBook(UsedBookReplyDto dto, Long userId) {
        UsedBook usedBook = usedBookRepository.findById(dto.getUsedBookId())
                .orElseThrow(() -> new IllegalArgumentException("해당 중고도서는 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Reply reply = Reply.createReplyUsedBook(dto, user, usedBook);

        if (replyRepository.save(reply) != null) {
            usedBook.getReplies().add(reply);
            user.getReplies().add(reply);
        } else {
            throw new IllegalArgumentException("중고도서에 댓글 작성 실패");
        }

        return UsedBookReplyDto.createReplyDto(reply, reply.getSubReply());
    }

    // 중고도서 댓글 수정
    public UsedBookReplyDto updateReplyOnUsedBook(UsedBookReplyDto dto, Long userId) {
        Reply reply = replyRepository.findById(dto.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (!reply.getUser().getId().equals(userId))
            throw new IllegalArgumentException("중고도서 댓글 수정 실패! 회원 정보가 일치하지 않습니다.");

        reply.patchUsedBook(dto);
        if (replyRepository.save(reply) == null)
            throw new IllegalArgumentException("중고도서 댓글 수정 실패");

        return UsedBookReplyDto.createReplyDto(reply, reply.getSubReply());
    }

    // 중고도서 댓글 삭제
    public String deleteReplyOnUsedBook(Long replyId, Long userId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));

        if (reply.getBoard() != null)
            throw new IllegalArgumentException("게시글에 대한 댓글입니다.");

        if (!reply.getUser().getId().equals(userId))
            throw new IllegalArgumentException("중고도서 댓글 삭제 실패! 회원 정보가 일치하지 않습니다.");

        replyRepository.delete(reply);
        return "댓글 삭제 완료 : " + reply.getContent();
    }

    // 중고도서 댓글 조회
    public Page<UsedBookReplyDto> usedBookReplyList(Long usedBookId, String page, String size) {
        UsedBook usedBook = usedBookRepository.findById(usedBookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 중고도서는 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("reply_date").descending());

        Page<Reply> replies = replyRepository.findAllByUsedBook(usedBookId, pageable);

        return replies.map(reply -> UsedBookReplyDto.createDto(reply));
    }
}
