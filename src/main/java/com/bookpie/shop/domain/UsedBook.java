package com.bookpie.shop.domain;

import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class UsedBook {

    @Id @GeneratedValue
    @Column(name = "used_id")
    private Long id;

    private String title;
    private String content;
    private int price;
    private int view;
    private LocalDateTime uploadDate;
    private BookState bookState;
    private SaleState saleState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "book")
    private List<UsedBookLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookTag> tags = new ArrayList<>();

}
