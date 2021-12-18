package com.bookpie.shop.domain;

import com.bookpie.shop.domain.enums.BoardType;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;
    private LocalDateTime boardDate;
    private int view;
    private BoardType boardType;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();
}
