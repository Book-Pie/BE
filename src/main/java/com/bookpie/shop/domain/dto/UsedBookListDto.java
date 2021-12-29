package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.Data;

@Data
public class UsedBookListDto {

    private Long id;
    private String title;
    private int price;
    private String image;


    public UsedBookListDto(UsedBook usedBook){
        this.id= usedBook.getId();
        this.title = usedBook.getTitle();
        this.price = usedBook.getPrice();
        this.image = usedBook.getThumbnail();

    }
}
