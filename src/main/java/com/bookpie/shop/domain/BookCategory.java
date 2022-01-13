package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
public class BookCategory {
    @Id
    @Column(name = "book_category_id")
    private Long Id;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private BookCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookCategory> subCategory = new ArrayList<>();

    public void setId(Long id) {
        this.Id = id;
    }

    public static BookCategory createCategory(BookCategoryDto dto, BookCategory parentCategory) {
        return BookCategory.builder()
                .Id(dto.getCategoryId())
                .categoryName(dto.getCategoryName())
                .parentCategory(parentCategory)
                .build();
    }

}
