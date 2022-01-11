package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
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
                                    " join fetch b.seller s" +
                                    " where o.id= :id",Order.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
    }

    public List<Order> findBySeller(Long id){
        return em.createQuery("select o from Order o" +
                                    " join fetch o.book b" +
                                    " join fetch b.seller s " +
                                    " where s.id= :id",Order.class)
                .setParameter("id",id)
                .getResultList();
    }

    public List<Order> findByBuyer(Long id){
        return em.createQuery("select o from Order o " +
                                    " join fetch o.buyer b " +
                                    " where b.id= :id",Order.class)
                .setParameter("id",id)
                .getResultList();
    }

    public boolean remove(Long id){
        Order order = findById(id).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다."));
        em.remove(order);
        return true;
    }
}