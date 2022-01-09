package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.UsedBook;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderBookDto {
    private Long bookId;
    private String title;
    private int price;
    private String image;

    public OrderBookDto(UsedBook book){
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.price = book.getPrice();
        this.image = book.getThumbnail();
    }
}
