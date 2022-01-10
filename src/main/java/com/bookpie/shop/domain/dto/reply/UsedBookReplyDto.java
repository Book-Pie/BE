package com.bookpie.shop.domain.dto.reply;

import com.bookpie.shop.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UsedBookReplyDto {
    private Long replyId;
    private Long parentReplyId;
    private Long usedBookId;
    private Long userId;
    private String content;
    private LocalDateTime replyDate;
    private String nickName;

    private List<SubReplyDto> subReply = new ArrayList<>();

    public UsedBookReplyDto(Long replyId, Long parentReplyId, Long usedBookId, Long userId, String content,
                            LocalDateTime replyDate, String nickName, List<Reply> replies) {
        this.replyId = replyId;
        this.usedBookId = usedBookId;
        this.userId = userId;
        this.content = content;
        this.replyDate = replyDate;
        this.nickName = nickName;
        this.parentReplyId = parentReplyId;
        if (parentReplyId == 0) {
            this.subReply = replies.stream().map(reply -> SubReplyDto.createDto(reply)).collect(Collectors.toList());
        }
    }

    public static UsedBookReplyDto createDto(Reply reply) {
        return new UsedBookReplyDto(reply.getId(), reply.getParentReply().getId(), reply.getUsedBook().getId(),
                reply.getUser().getId(), reply.getContent(), reply.getReplyDate(), reply.getUser().getNickName(),
                reply.getSubReply());
    }
}
