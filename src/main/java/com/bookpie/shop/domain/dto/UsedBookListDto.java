package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsedBookListDto {

    private Long id;
    private String title;
    private int price;
    private String image;
    private LocalDateTime uploadDate;
    private LocalDateTime modifiedDate;
    private SaleState state;
    private int likeCount;
    private int replyCount;

    public UsedBookListDto(UsedBook usedBook){
        this.id= usedBook.getId();
        this.title = usedBook.getTitle();
        this.price = usedBook.getPrice();
        this.image = usedBook.getThumbnail();
        this.uploadDate = usedBook.getUploadDate();
        this.modifiedDate = usedBook.getModifiedDate();
        this.likeCount = usedBook.getLikeCount();
        this.replyCount = usedBook.getReplyCount();
        this.state = usedBook.getSaleState();
    }
}
