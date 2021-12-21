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
        Optional<User> user = null;
        try {
            user = Optional.ofNullable(em.createQuery("select u from User u where u.nickName= :nickName", User.class)
                    .setParameter("nickName", nickName)
                    .getSingleResult());
        }catch (Exception e){
            user = Optional.empty();
        }finally {
            return user;
        }

    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User u where u.email= :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }



    public Optional<User> findByUsername(String username){
        Optional<User> user = null;
        try {
            user = Optional.ofNullable(em.createQuery("select u from User u where u.username= :username",User.class)
                    .setParameter("username",username)
                    .getSingleResult());
        }catch (Exception e){
            user = Optional.empty();
        }finally {
            return user;
        }

    }


}
