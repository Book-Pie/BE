package com.bookpie.shop.domain.dto.reply;

import com.bookpie.shop.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
public class BoardReplyDto {
    private Long replyId;
    private Long parentReplyId;
    private Long boardId;
    private Long userId;
    private String content;
    private LocalDateTime replyDate;
    private String nickName;

    private List<SubReplyDto> subReply = new ArrayList<>();

    public BoardReplyDto(Long reply_id, Long parent_reply_id, Long user_id, String nickName, String content,
                         LocalDateTime replyDate, List<Reply> replies, Long board_id) {
        this.replyId = reply_id;
        this.parentReplyId = parent_reply_id;
        this.userId = user_id;
        this.nickName = nickName;
        this.content = content;
        this.replyDate = replyDate;
        if (parent_reply_id == 0) {
            this.subReply = replies.stream().map(reply -> SubReplyDto.createDto(reply)).collect(Collectors.toList());
            this.subReply.sort(Comparator.comparing(SubReplyDto::getReplyDate).reversed());
        }
        this.boardId = board_id;
    }

    public static BoardReplyDto createDto(Reply reply) {
        return new BoardReplyDto(reply.getId(), reply.getParentReply().getId(), reply.getUser().getId(), reply.getUser().getNickName(),
                reply.getContent(), reply.getReplyDate(), reply.getSubReply(), reply.getBoard().getId());
    }

    // 댓글 작성 후 반환값
    public static  BoardReplyDto createReplyDto(Reply reply, List<Reply> subReplies) {
        return new BoardReplyDto(reply.getId(), reply.getUser().getId(), reply.getUser().getNickName(),
                reply.getContent(), reply.getReplyDate(), reply.getBoard().getId(), subReplies);
    }

    public BoardReplyDto(Long replyId, Long userId, String nickName, String content,
                         LocalDateTime replyDate, Long boardId, List<Reply> subReplies) {
        this.replyId = replyId;
        this.userId = userId;
        this.nickName = nickName;
        this.content = content;
        this.replyDate = replyDate;
        this.boardId = boardId;
        if (subReplies.size() != 0) {
            this.subReply = subReplies.stream().map(reply -> SubReplyDto.createDto(reply)).collect(Collectors.toList());
            this.subReply.sort(Comparator.comparing(SubReplyDto::getReplyDate).reversed());
        }
    }
}
