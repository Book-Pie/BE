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
    public List<UserReview> findByWriter(Long userId){
        return em.createQuery("select r from UserReview r" +
                            " join fetch r.order o" +
                            " join fetch o.book b" +
                            " join fetch b.seller" +
                            " join fetch o.buyer bu" +
                            " where bu.id= : userId")
                .setParameter("userId",userId)
                .getResultList();

    }


    //회원 리뷰 삭제
    public boolean remove(UserReview userReview){
        em.remove(userReview);
        return true;
    }
}
