package com.bookpie.shop.dto;

import com.bookpie.shop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {
    private Long userId;
    private Long usedBookId;
    private Address address;
    private String deliveryRequest;
}
