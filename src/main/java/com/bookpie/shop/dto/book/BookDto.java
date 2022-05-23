package com.bookpie.shop.dto.book;

import com.bookpie.shop.domain.Book;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDto {
    private Long bookId;
    private String title;
    private String author;
    private int categoryId;
    private String introduce;
    private int price;
    private String publisher;
    private Long isbn;

    public static BookDto createDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(),
                book.getCategory_id(), book.getIntroduction(), book.getPrice(),
                book.getPublisher(), book.getIsbn());
    }
}
