package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
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

    public BookCategory(Long category_id, String categoryName, BookCategory parentCategory) {
        this.Id = category_id;
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }
    public static BookCategory createCategory(BookCategoryDto dto, BookCategory parentCategory) {
        return new BookCategory(dto.getCategoryId(), dto.getCategoryName(), parentCategory);
    }

}
