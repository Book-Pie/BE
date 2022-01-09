package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.domain.enums.Category;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public UsedBook(User seller,String content,String title,int price,LocalDateTime uploadDate,BookState bookState,
                    SaleState saleState,Category first,Category second,String isbn){
        this.seller = seller;
        this.content = content;
        this.title = title;
        this.price = price;
        this.uploadDate = uploadDate;
        this.bookState = bookState;
        this.saleState = saleState;
        this.fstCategory = first;
        this.sndCategory = second;
        this.isbn = isbn;
        this.view = 0;
    }

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
        return UsedBook.builder()
                .seller(user)
                .content(dto.getContent())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .uploadDate(LocalDateTime.now())
                .bookState(dto.getState())
                .saleState(SaleState.SALE)
                .first(dto.getFstCategory())
                .second(dto.getSndCategory())
                .isbn(dto.getIsbn())
                .build();


    }
    public void setOrder(Order order){
        this.order = order;
    }

    //주문에 따른 판매상태 변경
    public void trading(){
        this.saleState = SaleState.TRADING;
    }
    public void cancel(){
        this.order = null;
        this.saleState = SaleState.SALE;
    }
    public void soldout(){
        this.saleState = SaleState.SOLD_OUT;
    }


}
