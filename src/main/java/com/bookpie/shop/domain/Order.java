package com.bookpie.shop.domain;

import com.bookpie.shop.domain.enums.OrderState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private UsedBook book;

    @OneToOne(fetch = FetchType.LAZY)
    private UserReview review;

    @Enumerated(value = EnumType.STRING)
    OrderState orderState;

    @Builder
    public Order(User buyer,UsedBook usedBook,Address address,LocalDateTime orderDate){
        this.book = usedBook;
        this.buyer = buyer;
        this.address = address;
        this.orderDate = orderDate;
        this.orderState = OrderState.TRADING;
    }

    public static Order createOrder(User user,Address address,UsedBook usedBook){
        Order order = Order.builder()
                           .usedBook(usedBook)
                           .buyer(user)
                           .address(address)
                           .orderDate(LocalDateTime.now())
                           .build();
        user.addOrder(order);
        usedBook.trading();
        usedBook.setOrder(order);
        return order;
    }

    public void addReview(UserReview review){
        this.review = review;
        review.addOrder(this);
    }

    public void removeReview(){
        this.review = null;
    }


}
