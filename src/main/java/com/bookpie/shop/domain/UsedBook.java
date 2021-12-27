package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private String thumbnail;
    @Enumerated(EnumType.STRING)
    private BookState bookState;

    @Enumerated(EnumType.STRING)
    private SaleState saleState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<UsedBookLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private Set<BookTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public void addBookTag(BookTag tag){
        tags.add(tag);
        tag.setBook(this);
    }

    public void addImage(Image image){
        images.add(image);
        image.setBook(this);
    }

    public void setThumbnail(String fileName){
        thumbnail=fileName;
    }

    public static UsedBook createUsedBook(User user,UsedBookCreateDto dto){
        UsedBook usedBook = new UsedBook();
        usedBook.seller = user;
        usedBook.content = dto.getContent();
        usedBook.title = dto.getTitle();
        usedBook.price = dto.getPrice();
        usedBook.uploadDate = LocalDateTime.now();
        usedBook.bookState = dto.getState();
        usedBook.saleState = SaleState.SALE;
        usedBook.bookState = dto.getState();
        return usedBook;
    }




}
