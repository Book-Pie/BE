package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Address;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String name;
    private String phone;
    private Address address;
}
