package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_category")
@Getter
@NoArgsConstructor
public class BookCategory {
    @Id
    @Column(name = "book_category_id")
    private Long Id;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private BookCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<BookCategory> subCategory = new ArrayList<>();

    public void setId(Long id) {
        this.Id = id;
    }

    // 부모 카테고리 생성자
    public BookCategory(Long category_id, String categoryName) {
        this.Id = category_id;
        this.categoryName = categoryName;
    }
    public static BookCategory createParentCategory(BookCategoryDto dto) {
        return new BookCategory(dto.getCategory_id(), dto.getCategoryName());
    }

    // 자식 카테고리 생성자
    public BookCategory(Long category_id, String categoryName, BookCategory bookCategory) {
        this.Id = category_id;
        this.categoryName = categoryName;
        this.parentCategory = bookCategory;
    }
    public static BookCategory createSubCategory(BookCategoryDto dto, BookCategory bookCategory) {
        return new BookCategory(dto.getCategory_id(), dto.getCategoryName(), bookCategory);
    }

}
