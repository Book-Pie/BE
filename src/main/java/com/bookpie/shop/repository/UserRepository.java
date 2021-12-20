package com.bookpie.shop.repository;

import com.bookpie.shop.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findById(Long id){
        return Optional.of(em.find(User.class,id));
    }

    public List<User> findByNickName(String nickName){
        return em.createQuery("select u from User u where u.nickName =:nickName",User.class)
                .setParameter("nickName",nickName)
                .getResultList();
    }

    public Optional<User> findByEmail(String email){
        return Optional.ofNullable(em.createQuery("select u from User u where u.email =:email",User.class)
                .setParameter("email",email)
                .getSingleResult()
                );
    }


}
