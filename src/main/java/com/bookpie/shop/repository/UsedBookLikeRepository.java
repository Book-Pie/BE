package com.bookpie.shop.repository;

import com.bookpie.shop.domain.QUsedBookLike;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.UsedBookLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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

    public void delete(UsedBookLike like){
        em.remove(like);
    }
    public boolean isLiked(Long bookId,Long userId){
        QUsedBookLike qUsedBookLike = QUsedBookLike.usedBookLike;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return  query.select(qUsedBookLike)
                         .from(qUsedBookLike)
                         .where(qUsedBookLike.book.id.eq(bookId), qUsedBookLike.user.id.eq(userId))
                         .fetchFirst() != null;
    }

    public Optional<UsedBookLike> findByUserIdAndBookId(Long userId,Long bookId){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QUsedBookLike qUsedBookLike = QUsedBookLike.usedBookLike;
        UsedBookLike like = query.select(qUsedBookLike)
                                 .from(qUsedBookLike)
                                 .where(qUsedBookLike.user.id.eq(userId), qUsedBookLike.book.id.eq(bookId))
                                 .fetchOne();
        return Optional.ofNullable(like);
    }
}
