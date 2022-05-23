package com.bookpie.shop.dto.reply;

import com.bookpie.shop.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SubReplyDto {
    private Long replyId;
    private Long parentReplyId;
    private Long userId;
    private String content;
    private LocalDateTime replyDate;
    private String nickName;
    private Boolean secret;
    private String profile;

    public SubReplyDto(Long reply_id, Long parent_reply_id, Long user_id, String nickName,
                       String content, LocalDateTime replyDate, Boolean secret, String profile) {
        this.replyId = reply_id;
        this.parentReplyId = parent_reply_id;
        this.userId = user_id;
        this.nickName = nickName;
        this.content = content;
        this.replyDate = replyDate;
        this.secret = secret;
        this.profile = profile;
    }

    public static SubReplyDto createDto(Reply subReply) {
        return new SubReplyDto(subReply.getId(), subReply.getParentReply().getId(), subReply.getUser().getId(),
                subReply.getUser().getNickName(), subReply.getContent(), subReply.getReplyDate(),
                subReply.getSecret(), subReply.getUser().getImage());
    }
}
