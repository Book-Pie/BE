package com.bookpie.shop.domain.dto.point;

import com.bookpie.shop.domain.OrderPoint;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
public class PointDto {
    private Long point_id;
    private Long user_id;
    private int amount;
    private int cancel_amount;
    // 결제번호
    private String imp_uid;
    private String merchant_uid;
    private LocalDateTime order_date;

    public PointDto(int amount, String imp_uid, String merchant_uid) {
        this.amount = 0;
        this.cancel_amount = amount;
        this.imp_uid = imp_uid;
        this.merchant_uid = merchant_uid;
    }

    public static PointDto createDto(OrderPoint orderPoint) {
        return new PointDto(orderPoint.getId(), orderPoint.getUser().getId(), orderPoint.getAmount(), orderPoint.getCancel_amount(),
                orderPoint.getImp_uid(), orderPoint.getMerchant_uid(), orderPoint.getOrder_date());
    }

    public static PointDto cancelDto(OrderPoint orderPoint) {
        return new PointDto(orderPoint.getAmount(), orderPoint.getImp_uid(), orderPoint.getMerchant_uid());
    }
}
