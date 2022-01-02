package com.bookpie.shop.repository;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.UsedBookLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsedBookLikeRepository {

    private final EntityManager em;

    public Optional<UsedBookLike> findById(Long id){
        return Optional.ofNullable(em.find(UsedBookLike.class,id));
    }

    public Optional<UsedBookLike> findByUserAndBook(Long userId,Long bookId){
        return em.createQuery("select ul from UsedBookLike ul" +
                            " where ul.book.id= :bookId and ul.user.id= :userId", UsedBookLike.class)
                .setParameter("bookId",bookId)
                .setParameter("userId",userId)
                .getResultList()
                .stream().findAny();
    }

    public Long save(UsedBookLike like){
        em.persist(like);
        return like.getId();
    }

    public void delete(Long id){
        UsedBookLike usedBookLike = findById(id).orElseThrow(() -> new EntityNotFoundException("등록된 좋아요가 없습니다."));
        em.remove(usedBookLike);
    }

}
