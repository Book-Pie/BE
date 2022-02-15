package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.enums.OrderState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderListDto {
    private Long orderId;
    private Long bookId;
    private Long reviewId;
    private String title;
    private String image;
    private int price;
    private String sellerNickName;
    private String buyerNickName;
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
        this.sellerNickName = order.getBook().getSeller().getNickName();
        this.buyerNickName = order.getBuyer().getNickName();
        if(order.getReview() != null){
            this.reviewId = order.getReview().getId();
        }
    }
}
