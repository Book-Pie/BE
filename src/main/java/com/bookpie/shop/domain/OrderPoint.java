package com.bookpie.shop.domain;

import com.bookpie.shop.dto.point.PointDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_point")
@Getter
@NoArgsConstructor
public class OrderPoint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imp_uid;

    private String merchant_uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int amount;

    private int cancel_amount;

    private LocalDateTime order_date;

    public OrderPoint(String imp_uid, String merchant_uid, User user, int amount, int cancel_amount) {
        this.imp_uid = imp_uid;
        this.merchant_uid = merchant_uid;
        this.user = user;
        this.cancel_amount = cancel_amount;
        this.amount = amount;
        this.order_date = LocalDateTime.now();
    }

    public static OrderPoint chargePoint(PointDto dto, User user) {
        return new OrderPoint(dto.getImpUid(), dto.getMerchantUid(), user, dto.getAmount(), dto.getCancelAmount());
    }

}
