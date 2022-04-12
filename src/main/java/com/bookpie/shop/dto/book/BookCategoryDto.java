package com.bookpie.shop.dto.book;

import com.bookpie.shop.domain.BookCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class BookCategoryDto {
    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private List<BookCategoryDto> subCategory;

    public BookCategoryDto(Long category_id, String categoryName, Long parent_id, List<BookCategory> bookCategoryList) {
        this.categoryId = category_id;
        this.categoryName = categoryName;
        this.parentId = parent_id;
        if (bookCategoryList != null) {
            this.subCategory = bookCategoryList.stream().map(category -> BookCategoryDto.createDto(category))
                .collect(Collectors.toList());
        }
    }

    public static BookCategoryDto createDto(BookCategory bookCategory) {
        return new BookCategoryDto(bookCategory.getId(), bookCategory.getCategoryName(),
                bookCategory.getParentCategory().getId(), bookCategory.getSubCategory());
    }
}
