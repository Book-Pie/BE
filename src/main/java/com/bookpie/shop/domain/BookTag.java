package com.bookpie.shop.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class BookTag {

    @Id @GeneratedValue
    @Column(name = "book_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_id")
    private UsedBook book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static BookTag createBookTag(Tag tag){
        BookTag bookTag = new BookTag();
        bookTag.tag = tag;
        return bookTag;
    }
    public void setBook(UsedBook book){
        this.book=book;
    }

}
