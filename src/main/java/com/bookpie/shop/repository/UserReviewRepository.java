package com.bookpie.shop.repository;

import com.bookpie.shop.domain.QUserReview;
import com.bookpie.shop.domain.UserReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserReviewRepository {

    private final EntityManager em;

    //회원 리뷰 저장
    public Long save(UserReview userReview){
        em.persist(userReview);
        return userReview.getId();
    }

    //회원 리뷰 조회
    public Optional<UserReview> findById(Long id){
        return em.createQuery("select r from UserReview r " +
                " join fetch r.order o " +
                " join fetch o.book b " +
                " join fetch b.seller s " +
               " where r.id= :id").setParameter("id",id)
                .getResultList().stream().findAny();
    }


    //내가 쓴 리뷰 조회
    public List<UserReview> findByWriter(Long userId,int limit,int offset){
        return em.createQuery("select r from UserReview r" +
                            " join fetch r.order o" +
                            " join fetch o.book b" +
                            " join fetch b.seller s" +
                            " join fetch o.buyer bu" +
                            " where bu.id= : userId")
                .setParameter("userId",userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }
    public Long countByWriter(Long userId){
        QUserReview qUserReview = QUserReview.userReview;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUserReview.count())
                .from(qUserReview)
                .where(qUserReview.order.buyer.id.eq(userId)).fetchOne();
    }

    //내게 달린 리뷰 조회
    public List<UserReview> findByReader(Long userId,int limit,int offset){
        return em.createQuery("select r from UserReview r" +
                                " join fetch r.order o" +
                                " join fetch o.book b" +
                                " join fetch b.seller s" +
                                " join fetch o.buyer bu" +
                                " where s.id= :userId")
                .setParameter("userId",userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long countByReader(Long userId){
        return (Long) em.createQuery("select count(r) from UserReview r" +
                                " where r.order.book.seller.id= :userId")
                        .setParameter("userId", userId)
                        .getSingleResult();

    }

    //회원 리뷰 삭제
    public boolean remove(UserReview userReview){
        em.remove(userReview);
        return true;
    }
}
