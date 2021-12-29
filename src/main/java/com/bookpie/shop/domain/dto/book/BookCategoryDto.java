package com.bookpie.shop.domain.dto.book;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookCategoryDto {
    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private List<BookCategoryDto> subCategory;
}
