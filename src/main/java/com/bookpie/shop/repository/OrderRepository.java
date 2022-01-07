package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public Long save(Order order){
        em.persist(order);
        return order.getId();
    }

    public Optional<Order> findById(Long id){
        return Optional.ofNullable(em.find(Order.class,id));
    }

    public Optional<Order> findDetailById(Long id){
        return em.createQuery("select o from Order o" +
                                    " join fetch o.buyer u " +
                                    " join fetch o.book b " +
                                    " where o.id= :id",Order.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
    }
}
