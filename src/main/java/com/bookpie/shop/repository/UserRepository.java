package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.QUser;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.enums.Grade;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        return Optional.ofNullable((em.find(User.class,id)));
    }

    public Optional<User> findByNickName(String nickName){
        List<User> users = em.createQuery("select u from User u where u.nickName= :nickName and u.grade<> :grade", User.class)
                .setParameter("nickName", nickName)
                .setParameter("grade",Grade.WITH_DRAW)
                .getResultList();
        return users.stream().findAny();

    }

    public Optional<User> findByEmail(String email){
        List<User> users =  em.createQuery("select u from User u where u.email= :email and u.grade<> :grade", User.class)
                .setParameter("email", email)
                .setParameter("grade",Grade.WITH_DRAW)
                .getResultList();
        return users.stream().findAny();
    }

    public Optional<User> findByEmailAllgrade(String email){
        QUser qUser = QUser.user;
        JPAQueryFactory query = new JPAQueryFactory(em);
        User user = query.select(qUser).where(qUser.email.eq(email)).fetchOne();
        return Optional.ofNullable(user);
    }

    public Optional<User> findByNameAndPhone(String name,String phone){
        QUser qUser = QUser.user;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return Optional.ofNullable(query.select(qUser)
                .from(qUser)
                .where(qUser.name.eq(name),qUser.phone.eq(phone))
                .fetchOne());
    }


    /*
        Jwt를 이용해 사용자 정보를 불러올 때 사용하는 메서드.
        fechjoin 을 이용해 한번의 쿼리로 User Entity의 roles를 가져오기
        위해 findByEmail 메서드와 분리함
     */
    public Optional<User> findByEmailWithRole(String email){
        List<User> users = em.createQuery("select distinct u from User u " +
                                            " join fetch u.roles" +
                                            " where u.email= :email" +
                                            " and u.grade<> :grade",User.class)
                .setParameter("email",email)
                .setParameter("grade", Grade.WITH_DRAW)
                .getResultList();
        return users.stream().findAny();
    }

    public Long count(){
        QUser qUser = QUser.user;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUser.count()).from(qUser).fetchOne();
    }


}
