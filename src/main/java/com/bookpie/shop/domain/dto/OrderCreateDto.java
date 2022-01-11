package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Address;
import lombok.Data;

@Data
public class OrderCreateDto {
    private Long userId;
    private Long usedBookId;
    private Address address;
}
