package com.bookpie.shop.dto;

import lombok.Data;

@Data
public class FindUserDto {
    private String name;
    private String phone;
    private String email;
    private String password;
}
