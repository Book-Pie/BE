package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderUserDto buyer;
    private OrderUserDto seller;
    private OrderBookDto book;
    private String deliveryRequest;

    @Builder
    public OrderDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.buyer = new OrderUserDto(order.getBuyer());
        this.seller = new OrderUserDto(order.getBook().getSeller());
        this.book = new OrderBookDto(order.getBook());
        this.deliveryRequest = order.getDeliveryRequest();
    }
}
