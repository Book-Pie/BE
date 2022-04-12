package com.bookpie.shop.dto;

import com.bookpie.shop.enums.Category;
import lombok.Data;

@Data
public class FindUsedBookDto {
    private String title;
    private Category fstCategory;
    private Category sndCategory;
    private int offset;
    private int limit;
    private String sort;
    private Long pageCount;

}
