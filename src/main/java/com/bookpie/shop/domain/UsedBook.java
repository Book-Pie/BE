package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.domain.enums.Category;
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
    private String isbn;
    @Enumerated(EnumType.STRING)
    private Category fstCategory;

    @Enumerated(EnumType.STRING)
    private Category sndCategory;

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

    @OneToMany(mappedBy = "book")
    private List<UsedBookLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private Set<BookTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "usedBook", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

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
        usedBook.fstCategory = dto.getFstCategory();
        usedBook.sndCategory = dto.getSndCategory();
        return usedBook;
    }

    //주문에 따른 판매상태 변경
    public void trading(){
        this.saleState = SaleState.TRADING;
    }

    public void cancel(){
        this.saleState = SaleState.SALE;
    }
    public void soldout(){
        this.saleState = SaleState.SOLD_OUT;
    }


}
