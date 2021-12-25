package com.bookpie.shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String fileName;


    @ManyToOne(fetch = FetchType.LAZY)
    private UsedBook book;

    public void setBook(UsedBook book) {
        this.book = book;
    }

    public Image(String fileName) {
        this.fileName = fileName;
    }
}
