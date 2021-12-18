package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Reply {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    private String content;
    private LocalDateTime replyDate;

    @ManyToOne
    private Board board;

    @ManyToOne
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_id")
    private UsedBook usedBook;
}
