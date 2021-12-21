package com.bookpie.shop.repository;

import com.bookpie.shop.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public Optional<User> findById(Long id){
        return Optional.of(em.find(User.class,id));
    }

    public Optional<User> findByNickName(String nickName){
        List<User> users = em.createQuery("select u from User u where u.nickName= :nickName", User.class)
                .setParameter("nickName", nickName)
                .getResultList();
        return users.stream().findAny();

    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User u where u.email= :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }



    public Optional<User> findByUsername(String username){
        List users = em.createQuery("select u from User u where u.username= :username")
                .setParameter("username", username)
                .getResultList();
        return users.stream().findAny();

    }


}
