package com.bookpie.shop.repository;

import com.bookpie.shop.domain.UserReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        return Optional.ofNullable(em.find(UserReview.class,id));
    }
}
