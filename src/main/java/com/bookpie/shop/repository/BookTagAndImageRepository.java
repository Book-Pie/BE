package com.bookpie.shop.repository;

import com.bookpie.shop.domain.BookTag;
import com.bookpie.shop.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BookTagAndImageRepository {

    private final EntityManager em;

    public void removeBookTag(BookTag bookTag){
        em.remove(bookTag);
    }

    public void removeImage(Image image){
        em.remove(image);
    }

}

