package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.reply.BoardReplyDto;
import com.bookpie.shop.domain.dto.reply.SubReplyDto;
import com.bookpie.shop.domain.dto.reply.UsedBookReplyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String content;
    private LocalDateTime replyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private Reply parentReply;

    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL)
    private List<Reply> subReply = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_id")
    private UsedBook usedBook;

    @Column(columnDefinition = "boolean default false")
    private Boolean secret;

    // 게시글 댓글 생성
    public Reply(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
        this.replyDate = LocalDateTime.now();
        this.secret = false;
    }
    public static Reply createReplyBoard(BoardReplyDto dto, User user, Board board) {
        return new Reply(dto.getContent(), user, board);
    }

    // 대댓글 생성
    public Reply(String content, User user, Reply parentReply, Boolean secret) {
        this.content = content;
        this.secret = secret;
        this.user = user;
        this.parentReply = parentReply;
        this.replyDate = LocalDateTime.now();
    }
    public static Reply createSubReplyBoard(SubReplyDto dto, User user, Reply parentReply, Boolean secret) {
        return new Reply(dto.getContent(), user, parentReply, secret);
    }

    // 중고도서 댓글
    public static Reply createReplyUsedBook(UsedBookReplyDto dto, User user, UsedBook usedBook) {
        return new Reply(dto.getContent(), dto.getSecret(), user, usedBook);
    }
    public Reply(String content, Boolean secret, User user, UsedBook usedBook) {
        this.replyDate = LocalDateTime.now();
        this.content = content;
        this.secret = secret;
        this.user = user;
        this.usedBook = usedBook;
    }

    // 게시글 댓글 수정
    public void patch(BoardReplyDto dto) {
        if (dto.getContent() != null) {
            this.content = dto.getContent();
            this.replyDate = LocalDateTime.now();
        }
    }

    // 중고도서 댓글 수정
    public void patchUsedBook(UsedBookReplyDto dto) {
        if (dto.getContent() != null) {
            this.content = dto.getContent();
            this.replyDate = LocalDateTime.now();
        }
    }

}
