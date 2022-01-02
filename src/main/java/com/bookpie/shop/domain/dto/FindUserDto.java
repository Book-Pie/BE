package com.bookpie.shop.domain.dto;

import lombok.Data;

@Data
public class FindUserDto {
    private String username;
    private String name;
    private String phone;
    private String email;
    private String password;
}
