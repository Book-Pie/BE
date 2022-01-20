package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.enums.OrderState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderListDto {
    private Long orderId;
    private Long bookId;
    private String title;
    private String image;
    private int price;
    private OrderState state;
    private LocalDateTime orderDate;

    public OrderListDto(Order order){
        this.orderId = order.getId();
        this.bookId = order.getBook().getId();
        this.title = order.getBook().getTitle();
        this.image = order.getBook().getThumbnail();
        this.price = order.getBook().getPrice();
        this.state = order.getOrderState();
        this.orderDate = order.getOrderDate();
    }
}
