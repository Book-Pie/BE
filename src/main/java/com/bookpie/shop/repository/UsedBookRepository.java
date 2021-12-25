package com.bookpie.shop.repository;

import com.bookpie.shop.domain.UsedBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsedBookRepository {

    private final EntityManager em;

    public Long save(UsedBook usedBook){
        em.persist(usedBook);
        return usedBook.getId();
    }

    public List<UsedBook> findByUserId(Long userId){
        return em.createQuery("select ub from UsedBook  ub" +
                                " join fetch ub.seller s" +
                                " join fetch ub.tags t" +
                                " join fetch ub.images" +
                                " where s.id= : userId",UsedBook.class)
                .setParameter("userId",userId)
                .getResultList();
    }

    public Optional<UsedBook> findById(Long id){
        return em.createQuery("select distinct ub from UsedBook ub" +
                                    " join fetch ub.seller s" +
                                    " join fetch ub.tags bt" +
                                    " join fetch bt.tag t" +
                                    " where ub.id= :id",UsedBook.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
    }
}
