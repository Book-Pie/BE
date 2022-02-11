package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Image;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.enums.BookState;
import com.bookpie.shop.domain.enums.SaleState;
import lombok.Data;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UsedBookDto {
    private Long usedBookId;
    private Long sellerId;
    private String sellerName;
    private String sellerImage;
    private List<JSONObject> favoriteCategories;
    private int totalSales;
    private int price;
    private String title;
    private String content;
    private LocalDateTime uploadDate;
    private LocalDateTime modifiedDate;
    private int view;
    private BookState bookState;
    private SaleState saleState;
    private String fstCategory;
    private String sndCategory;
    private int likeCount;
    private int replyCount;

    private List<String> tags = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    public static UsedBookDto createUsedBookDto(UsedBook usedBook){
        UsedBookDto dto = new UsedBookDto();
        dto.setUsedBookId(usedBook.getId());
        dto.setSellerId(usedBook.getSeller().getId());
        dto.setSellerName(usedBook.getSeller().getNickName());
        dto.setSellerImage(usedBook.getSeller().getImage());
        dto.setPrice(usedBook.getPrice());
        dto.setTitle(usedBook.getTitle());
        dto.setContent(usedBook.getContent());
        dto.setUploadDate(usedBook.getUploadDate());
        dto.setModifiedDate(usedBook.getModifiedDate());
        dto.setBookState(usedBook.getBookState());
        dto.setView(usedBook.getView());
        dto.setSaleState(usedBook.getSaleState());
        dto.setView(usedBook.getView());
        dto.setFstCategory(usedBook.getFstCategory().getKr());
        dto.setSndCategory(usedBook.getSndCategory().getKr());
        dto.setLikeCount(usedBook.getLikeCount());
        dto.setReplyCount(usedBook.getReplyCount());
        List<Image> images = usedBook.getImages();
        images.stream().forEach(image -> dto.images.add(image.getFileName()));
        usedBook.getTags().stream()
                .forEach(tag->dto.tags.add(tag.getTag().getName()));
        dto.setTotalSales(usedBook.getSeller().getBookCount());
        return dto;
    }

    public void addSellerCategories(List<JSONObject> favoriteCategories){
        this.favoriteCategories = favoriteCategories;
    }

}
