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
    private Long pointId;
    private Long userId;
    private int amount;
    private int cancelAmount;
    // 결제번호
    private String impUid;
    private String merchantUid;
    private LocalDateTime orderDate;

    public PointDto(int amount, String imp_uid, String merchant_uid) {
        this.amount = 0;
        this.cancelAmount = amount;
        this.impUid = imp_uid;
        this.merchantUid = merchant_uid;
    }

    public static PointDto createDto(OrderPoint orderPoint) {
        return new PointDto(orderPoint.getId(), orderPoint.getUser().getId(), orderPoint.getAmount(), orderPoint.getCancel_amount(),
                orderPoint.getImp_uid(), orderPoint.getMerchant_uid(), orderPoint.getOrder_date());
    }

    public static PointDto cancelDto(OrderPoint orderPoint) {
        return new PointDto(orderPoint.getAmount(), orderPoint.getImp_uid(), orderPoint.getMerchant_uid());
    }
}
